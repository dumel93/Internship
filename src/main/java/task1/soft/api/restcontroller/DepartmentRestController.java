package task1.soft.api.restcontroller;

import lombok.RequiredArgsConstructor;
import org.apache.el.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(produces = "application/json", value = "/departments")
public class DepartmentRestController {

    private final DepartmentService departmentService;
    private final ModelMapper modelMapper;
    private final UserService userService;

    // -------------------getDepartments-------------------------------------------
    @Secured("ROLE_HEAD")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<DepartmentDTO> getDepartments(@AuthenticationPrincipal UserDetails auth,
                                              @RequestParam(defaultValue = "0") Integer offset,
                                              @RequestParam(defaultValue = "5") Integer limit,
                                              @RequestParam(defaultValue = "id") String sortBy,
                                              @RequestParam(defaultValue = "asc") String orderBy

    ){
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        List<Department> departments = departmentService.findAll(offset,limit,sortBy,orderBy);

        List<DepartmentDTO> departmentDTOS=departments.stream()
                .map(entity -> modelMapper.map(entity, DepartmentDTO.class))
                .collect(Collectors.toList());

        return departmentDTOS.stream().map(entity->departmentService.setEmployeesDetails(entity.getId())).collect(Collectors.toList());
    }

    @Secured("ROLE_HEAD")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    public DepartmentDTO getDepartment(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails auth) {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        Department department = departmentService.findOne(id);
        DepartmentDTO departmentDTO=modelMapper.map(department, DepartmentDTO.class);
        return departmentService.setEmployeesDetails(departmentDTO.getId());

    }

    // -------------------createDepartment-------------------------------------------
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createDepartment(@RequestBody DepartmentDTO departmentDTO, @AuthenticationPrincipal UserDetails auth) throws ParseException {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        Department department = modelMapper.map(departmentDTO, Department.class);
        departmentService.createDepartment(department.getName(), department.getCity());
    }

    // -------------------UpdateDepartment-------------------------------------------
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void UpdateDepartment(@RequestBody DepartmentDTO departmentDTO, @PathVariable Long id, @AuthenticationPrincipal UserDetails auth) throws ParseException {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        Department department = modelMapper.map(departmentDTO, Department.class);
        department.setId(id);
        departmentService.updateDepartment(department);

    }
    @Secured("ROLE_HEAD")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/salary/{id}")
    void setMinSalaryOrMaxSalary(@RequestBody DepartmentSalariesDTO departmentSalariesDTO, @PathVariable Long id, @AuthenticationPrincipal UserDetails auth) throws ParseException {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        Department department = departmentService.findOne(id);
        department.setMinSalary(departmentSalariesDTO.getMinSalary());
        department.setMaxSalary(departmentSalariesDTO.getMaxSalary());
        department.setId(id);
        departmentService.updateDepartment(department);

    }

    // -------------------deleteDepartment-------------------------------------------
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    void deleteDepartment(@PathVariable @Min(value = 1, message = "must be greater than or equal to 1") Long id, @AuthenticationPrincipal UserDetails auth) {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        Department department = departmentService.findOne(id);
        if (department.getEmployees().size() == 0) {
            departmentService.delete(department);

        }

    }
}
