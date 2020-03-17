package task1.soft.api.restcontroller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import task1.soft.api.dto.DepartmentDTO;
import task1.soft.api.dto.DepartmentSalariesDTO;
import task1.soft.api.entity.Department;
import task1.soft.api.exception.NoDeletePermissionException;
import task1.soft.api.exception.NotFoundException;
import task1.soft.api.service.DepartmentService;
import task1.soft.api.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/departments")
public class DepartmentRestController {

    private final DepartmentService departmentService;
    private final ModelMapper modelMapper;
    private final UserService userService;

    // -------------------getDepartments-------------------------------------------
    @PreAuthorize("hasRole('ROLE_HEAD') or hasRole('ROLE_CEO')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<DepartmentDTO> getDepartments(@AuthenticationPrincipal UserDetails auth) {

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        List<Department> departments = departmentService.findAllDepartments();
        return departments.stream()
                .map(department -> modelMapper.map(department, DepartmentDTO.class))
                .map(department -> departmentService.getAllDepartmentDetails(department.getId()))
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ROLE_HEAD') or hasRole('ROLE_CEO')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public DepartmentDTO getDepartment(@PathVariable("id") Long id,
                                       @AuthenticationPrincipal UserDetails auth) throws NotFoundException {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        Department department = departmentService.findDepartment(id);
        return departmentService.getAllDepartmentDetails(department.getId());

    }

    // -------------------createDepartment-------------------------------------------

    @PreAuthorize("hasRole('ROLE_CEO')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentDTO createDepartment(@Valid @RequestBody DepartmentDTO departmentDTO, @AuthenticationPrincipal UserDetails auth) {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        Department departmentCreated = departmentService.createDepartment(departmentDTO);
        departmentDTO.setId(departmentCreated.getId());
        return departmentService.getAllDepartmentDetails(departmentCreated.getId());
    }

    // -------------------UpdateDepartment-------------------------------------------
    @PreAuthorize("hasRole('ROLE_CEO')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DepartmentDTO updateDepartment(@Valid @RequestBody DepartmentDTO departmentDTO, @PathVariable Long id, @AuthenticationPrincipal UserDetails auth) throws NotFoundException {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        departmentService.findDepartment(id);
        departmentDTO.setId(id);
        Department department = modelMapper.map(departmentDTO, Department.class);
        departmentService.updateDepartment(departmentDTO);
        return departmentService.getAllDepartmentDetails(department.getId());

    }

    @PreAuthorize("hasRole('ROLE_CEO')")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/salary")
    public DepartmentDTO setMinSalaryAndMaxSalary(@Valid @RequestBody DepartmentSalariesDTO departmentSalariesDTO, @PathVariable Long id, @AuthenticationPrincipal UserDetails auth) throws NotFoundException {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        Department department = departmentService.findDepartment(id);
        department.setMinSalary(departmentSalariesDTO.getMinSalary());
        department.setMaxSalary(departmentSalariesDTO.getMaxSalary());
        department.setId(id);
        DepartmentDTO departmentDTO = modelMapper.map(department, DepartmentDTO.class);
        departmentService.updateDepartment(departmentDTO);
        return departmentService.getAllDepartmentDetails(department.getId());

    }

    // -------------------deleteDepartment-------------------------------------------
    @PreAuthorize("hasRole('ROLE_CEO')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void deleteDepartment(@PathVariable Long id, @AuthenticationPrincipal UserDetails auth) throws NotFoundException, NoDeletePermissionException {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        departmentService.deleteDepartment(id);
    }
}
