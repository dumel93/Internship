package task1.soft.api.restcontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import task1.soft.api.dto.DepartmentDTO;
import task1.soft.api.dto.DepartmentSalariesDTO;
import task1.soft.api.entity.Department;
import task1.soft.api.service.DepartmentService;
import task1.soft.api.util.CustomErrorType;
import task1.soft.api.util.SetterLoginTime;

import javax.validation.constraints.Min;
import java.util.List;

@Validated
@Secured("ROLE_CEO")
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(produces = "application/json", value = "/departments")
public class DepartmentRestController {


    private final DepartmentService departmentService;
    private final ModelMapper modelMapper;

    // -------------------getDepartments-------------------------------------------
    @Secured("ROLE_HEAD")
    @GetMapping
    public ResponseEntity<List<Department>> getDepartments(@AuthenticationPrincipal UserDetails auth) {
        SetterLoginTime.setLoginTime(auth);
        List<Department> departments = departmentService.findAll();
        if (departments.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Department>>(departments, HttpStatus.OK);
    }

    @Secured("ROLE_HEAD")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Department> getDepartment(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails auth) {
        SetterLoginTime.setLoginTime(auth);
        Department department = departmentService.findOne(id);
        if (department == null) {
            return new ResponseEntity(new CustomErrorType("Department with id " + id
                    + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Department>(department, HttpStatus.OK);
    }

    // -------------------createDepartment-------------------------------------------
    @PostMapping
    public ResponseEntity<String> createDepartment(@RequestBody DepartmentDTO departmentDTO, @AuthenticationPrincipal UserDetails auth) throws ParseException {
        SetterLoginTime.setLoginTime(auth);
        Department department = modelMapper.map(departmentDTO, Department.class);
        departmentService.createDepartment(department.getName(), department.getCity());
        return new ResponseEntity<String>(HttpStatus.CREATED);

    }

    // -------------------UpdateDepartment-------------------------------------------
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Department> UpdateDepartment(@RequestBody DepartmentDTO departmentDTO, @PathVariable Long id, @AuthenticationPrincipal UserDetails auth) throws ParseException {
        SetterLoginTime.setLoginTime(auth);
        Department department = modelMapper.map(departmentDTO, Department.class);
        department.setId(id);
        departmentService.updateDepartment(department);
        return new ResponseEntity<Department>(department, HttpStatus.OK);
    }

    @PutMapping("/salary/{id}")
    ResponseEntity<Department> setMinSalaryOrMaxSalary(@RequestBody DepartmentSalariesDTO departmentSalariesDTO, @PathVariable Long id, @AuthenticationPrincipal UserDetails auth) throws ParseException {
        SetterLoginTime.setLoginTime(auth);
        Department department = modelMapper.map(departmentSalariesDTO, Department.class);
        department.setMinSalary(department.getMinSalary());
        department.setMaxSalary(department.getMaxSalary());
        department.setId(id);
        departmentService.updateDepartment(department);
        return new ResponseEntity<Department>(department, HttpStatus.OK);

    }

    // -------------------deleteDepartment-------------------------------------------
    @DeleteMapping("/{id}")
    ResponseEntity deleteDepartment(@PathVariable @Min(value = 1, message = "must be greater than or equal to 1") Long id, @AuthenticationPrincipal UserDetails auth) {
        SetterLoginTime.setLoginTime(auth);
        Department department = departmentService.findOne(id);
        if (department.getEmployees().size() == 0) {
            departmentService.delete(department);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity(new CustomErrorType("Unable to delete. There are still employees in this department"),
                HttpStatus.CONFLICT);
    }
}
