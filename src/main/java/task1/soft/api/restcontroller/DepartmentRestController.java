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
import task1.soft.api.util.SearchCriteria;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Validated
@Secured("ROLE_CEO")
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
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
                                              @RequestParam(defaultValue = "0") Integer offset,
                                              @RequestParam(defaultValue = "5") Integer limit,
                                              @RequestParam(defaultValue = "id") String sortBy,
                                              @RequestParam(defaultValue = "asc") String orderBy

    ) {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        List<Department> departments;
        departments = departmentService.findAll(offset, limit, sortBy, orderBy);
        List<SearchCriteria> params = new ArrayList<>();
        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w+?)([:<>])(\\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                params.add(new SearchCriteria(matcher.group(1),
                        matcher.group(2), matcher.group(3)));
            }

            departments = departmentService.searchDepartment(params);
        }

        List<DepartmentDTO> departmentDTOS = departments.stream()
                .map(entity -> modelMapper.map(entity, DepartmentDTO.class))
                .collect(Collectors.toList());

        return departmentDTOS.stream()
                .map(entity -> departmentService.setEmployeesDetails(entity.getId()))
                .collect(Collectors.toList());
    }


    @Secured({"ROLE_HEAD", "ROLE_EMPLOYEE"})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public DepartmentDTO getDepartment(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails auth) {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        Department department = departmentService.findOne(id);
        return departmentService.setEmployeesDetails(department.getId());


    }

    // -------------------createDepartment-------------------------------------------
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentDTO createDepartment(@RequestBody DepartmentDTO departmentDTO, @AuthenticationPrincipal UserDetails auth) throws ParseException {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        Department department = modelMapper.map(departmentDTO, Department.class);
        Department departmentCreated = departmentService.createDepartment(department.getName(), department.getCity());
        return departmentService.setEmployeesDetails(departmentCreated.getId());
    }

    // -------------------UpdateDepartment-------------------------------------------
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DepartmentDTO updateDepartment(@RequestBody DepartmentDTO departmentDTO, @PathVariable Long id, @AuthenticationPrincipal UserDetails auth) throws ParseException {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        Department department = modelMapper.map(departmentDTO, Department.class);
        department.setId(id);
        departmentService.updateDepartment(department);
        return departmentService.setEmployeesDetails(department.getId());

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
        return departmentService.setEmployeesDetails(department.getId());

    }

    // -------------------deleteDepartment-------------------------------------------
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void deleteDepartment(@PathVariable @Min(value = 1, message = "must be greater than or equal to 1") Long id, @AuthenticationPrincipal UserDetails auth) {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        departmentService.delete(id);
    }
}
