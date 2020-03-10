package task1.soft.api.restcontroller;

import lombok.RequiredArgsConstructor;
import org.apache.el.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import task1.soft.api.dto.DepartmentDTO;
import task1.soft.api.dto.DepartmentSalariesDTO;
import task1.soft.api.entity.Department;
import task1.soft.api.service.DepartmentService;
import task1.soft.api.service.UserService;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@Secured("ROLE_CEO")
@RestController
@RequiredArgsConstructor
@RequestMapping("/departments")
public class DepartmentRestController {

    private final DepartmentService departmentService;
    private final ModelMapper modelMapper;
    private final UserService userService;

    // -------------------getDepartments-------------------------------------------
    @Secured("ROLE_HEAD")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<DepartmentDTO> getDepartments(@AuthenticationPrincipal UserDetails auth,
                                              @RequestParam(value = "search", required = false) String search,
                                              @RequestParam(defaultValue = "0") Integer id,
                                              @RequestParam String name,
                                              @RequestParam String city,
                                              @RequestParam(defaultValue = "0") Integer offset,
                                              @RequestParam(defaultValue = "5") Integer limit,
                                              @RequestParam(defaultValue = "id") String sortBy,
                                              @RequestParam(defaultValue = "asc") String orderBy

    ) {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        List<Department> departments;
        departments = departmentService.findAll(offset, limit, sortBy, orderBy);

        return departments.stream()
                .map(entity -> modelMapper.map(entity, DepartmentDTO.class))
                .collect(Collectors.toList())
                .stream()
                .map(entity -> departmentService.getAllDepartmentDetails(entity.getId()))
                .collect(Collectors.toList());
    }


    @Secured({"ROLE_HEAD", "ROLE_EMPLOYEE"})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public DepartmentDTO getDepartment(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails auth) {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        Department department = departmentService.findOne(id);
        return departmentService.getAllDepartmentDetails(department.getId());

    }

    // -------------------createDepartment-------------------------------------------
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentDTO createDepartment(@RequestBody DepartmentDTO departmentDTO, @AuthenticationPrincipal UserDetails auth) throws ParseException {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        Department department = modelMapper.map(departmentDTO, Department.class);
        Department departmentCreated = departmentService.createDepartment(department.getName(), department.getCity());
        return departmentService.getAllDepartmentDetails(departmentCreated.getId());
    }

    // -------------------UpdateDepartment-------------------------------------------
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DepartmentDTO updateDepartment(@RequestBody DepartmentDTO departmentDTO, @PathVariable Long id, @AuthenticationPrincipal UserDetails auth) throws ParseException {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        Department department = modelMapper.map(departmentDTO, Department.class);
        department.setId(id);
        departmentService.updateDepartment(department);
        return departmentService.getAllDepartmentDetails(department.getId());

    }

    @Secured("ROLE_HEAD")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/salary")
    public DepartmentDTO setMinSalaryAndMaxSalary(@RequestBody DepartmentSalariesDTO departmentSalariesDTO, @PathVariable Long id, @AuthenticationPrincipal UserDetails auth) throws ParseException {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        Department department = departmentService.findOne(id);
        department.setMinSalary(departmentSalariesDTO.getMinSalary());
        department.setMaxSalary(departmentSalariesDTO.getMaxSalary());
        department.setId(id);
        departmentService.updateDepartment(department);
        return departmentService.getAllDepartmentDetails(department.getId());

    }

    // -------------------deleteDepartment-------------------------------------------
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void deleteDepartment(@PathVariable @Min(value = 1, message = "must be greater than or equal to 1") Long id, @AuthenticationPrincipal UserDetails auth) {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        departmentService.delete(id);
    }
}
