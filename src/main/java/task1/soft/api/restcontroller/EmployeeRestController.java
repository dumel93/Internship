package task1.soft.api.restcontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import task1.soft.api.dto.EmployeeDTO;
import task1.soft.api.dto.EmployeePasswordDTO;
import task1.soft.api.dto.EmployeeSalaryDTO;
import task1.soft.api.entity.Department;
import task1.soft.api.entity.User;
import task1.soft.api.service.DepartmentService;
import task1.soft.api.service.UserService;
import task1.soft.api.util.CustomErrorType;
import task1.soft.api.util.SetterLoginTime;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;

@Validated
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(value = "/employees", produces = "application/json")
public class EmployeeRestController {


    private final UserService userService;
    private final DepartmentService departmentService;
    private final ModelMapper modelMapper;

    // -------------------Get an Employee/s-------------------------------------------
    @Secured({"ROLE_CEO", "ROLE_HEAD"})
    @GetMapping
    public ResponseEntity<List<User>> getAllEmployees(@AuthenticationPrincipal UserDetails auth) {
        SetterLoginTime.setLoginTime(auth);
        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @Secured({"ROLE_CEO", "ROLE_HEAD"})
    @GetMapping("/{id}")
    public ResponseEntity<User> getEmployeeById(@PathVariable Long id, @AuthenticationPrincipal UserDetails auth) {
        SetterLoginTime.setLoginTime(auth);
        User user = userService.findUser(id);
        if (user == null) {
            log.error("User with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("User with id " + id
                    + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }


    @Secured({"ROLE_HEAD", "ROLE_EMPLOYEE"})
    @GetMapping("/departments")
    public ResponseEntity<List<User>> findAllEmployeesOfDepartment(@AuthenticationPrincipal UserDetails auth) {
        SetterLoginTime.setLoginTime(auth);
        User currentUser = userService.findByEmail(auth.getUsername());
        Department department = currentUser.getDepartment();
        List<User> users = userService.findAllEmployeesOfDepartment(department.getId());
        if (users.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);

    }

    @Secured({"ROLE_CEO", "ROLE_HEAD"})
    @GetMapping("/departments/{id}")
    public ResponseEntity<List<User>> findEmployeesOfDepartment(@PathVariable Long id, @AuthenticationPrincipal UserDetails auth) {

        SetterLoginTime.setLoginTime(auth);

        List<User> users = userService.findAllEmployeesOfDepartment(id);
        if (users.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    // -------------------Create an Employee-------------------------------------------
    @Secured("ROLE_CEO")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    public ResponseEntity createEmployee(@AuthenticationPrincipal UserDetails auth, @RequestBody EmployeeDTO employeeDTO) {
        User employee = modelMapper.map(employeeDTO, User.class);
        SetterLoginTime.setLoginTime(auth);
        log.info("Creating User : {}", employee);
        if (userService.isEmailExist(employee)) {
            log.error("Unable to create. A User with email {} already exist", employee.getEmail());
            return new ResponseEntity(new CustomErrorType("Unable to create. A User with email " +
                    employee.getEmail() + " already exist."), HttpStatus.CONFLICT);
        }
        userService.createEmployee(employee.getFirstName(), employee.getLastName(), employee.getEmail(), employee.getPassword(), employee.getSalary(),employee.getDepartment());
        return new ResponseEntity<String>(HttpStatus.CREATED);
    }

    @Secured("ROLE_HEAD")
    @PostMapping("/departments")
    @ResponseStatus(HttpStatus.CREATED)
    public Object createEmployeeInHeadDepartment(@AuthenticationPrincipal UserDetails auth, @RequestBody EmployeeDTO employeeDTO) {

        SetterLoginTime.setLoginTime(auth);
        User employee = modelMapper.map(employeeDTO, User.class);

        if (userService.isEmailExist(employee)) {
            log.error("Unable to create. A User with email {} already exist", employee.getEmail());
            return new ResponseEntity(new CustomErrorType("Unable to create. A User with email " +
                    employee.getEmail() + " already exist."), HttpStatus.CONFLICT);
        }
        Department department = userService.findByEmail(auth.getUsername()).getDepartment();
        department.getEmployees().add(employee);
        departmentService.updateDepartment(department);
        employee.setDepartment(department);
        userService.createEmployee(employee.getFirstName(), employee.getLastName(), employee.getEmail(), employee.getPassword(), employee.getSalary(),employee.getDepartment());
        return new ResponseEntity<String>(HttpStatus.CREATED);

    }

    // -------------------Update an Employee-------------------------------------------

    @Secured("ROLE_HEAD")
    @PutMapping("/{id}")
    public ResponseEntity updateEmployee(@AuthenticationPrincipal UserDetails auth, @RequestBody EmployeePasswordDTO employeePasswordDTO, @PathVariable Long id) {

        SetterLoginTime.setLoginTime(auth);
        User employee = modelMapper.map(employeePasswordDTO, User.class);
        employee.setId(id);
        Department department = userService.findByEmail(auth.getUsername()).getDepartment();
        if (!userService.findAllEmployeesOfDepartment(department.getId()).contains(employee)) {
            log.error("Unable to update. User with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Unable to update. User with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        userService.updateUser(employee);
        return new ResponseEntity<User>(employee, HttpStatus.OK);
    }

    @Secured("ROLE_CEO")
    @PutMapping("/departments/head/{id}")
    public ResponseEntity<User> setHead(@PathVariable Long id, @AuthenticationPrincipal UserDetails auth) {

        SetterLoginTime.setLoginTime(auth);
        User employee = userService.findUser(id);
        Department departmentOFHead = userService.findByEmail(auth.getUsername()).getDepartment();
        if (employee == null || !userService.findAllEmployeesOfDepartment(departmentOFHead.getId()).contains(employee)) {
            log.error("Unable to update. User with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Unable to update. User with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }

        employee.setHead(true);
        userService.updateUser(employee);
        return new ResponseEntity<User>(employee, HttpStatus.OK);
    }


    @Secured("ROLE_HEAD")
    @PutMapping("{id}/password/")
    public ResponseEntity updatePassword(@AuthenticationPrincipal UserDetails auth, @RequestBody EmployeePasswordDTO employeePasswordDTO, @PathVariable Long id) {

        SetterLoginTime.setLoginTime(auth);
        User employee = userService.findUser(id);
        Department departmentOFHead = userService.findByEmail(auth.getUsername()).getDepartment();
        if (employee == null || !userService.findAllEmployeesOfDepartment(departmentOFHead.getId()).contains(employee)) {
            log.error("Unable to update. User with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Unable to update. User with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        String newPassword = employeePasswordDTO.getPassword();
        userService.updatePassword(employee, newPassword);
        return new ResponseEntity<User>(employee, HttpStatus.OK);
    }

    @Secured("ROLE_HEAD")
    @PutMapping("/salary/{id}")
    public ResponseEntity setSalary(@AuthenticationPrincipal UserDetails auth, @RequestBody EmployeeSalaryDTO employeeSalaryDTO, @PathVariable Long id) {

        SetterLoginTime.setLoginTime(auth);
        User employee = modelMapper.map(employeeSalaryDTO, User.class);
        Long departmentId = userService.findByEmail(auth.getUsername()).getDepartment().getId();
        if (employee == null || !userService.findAllEmployeesOfDepartment(departmentId).contains(employee)) {
            log.error("Unable to update. User with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Unable to update. User with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        BigDecimal salary = employee.getSalary();
        if (salary.compareTo(employee.getDepartment().getMinSalary()) >= 0 && salary.compareTo(employee.getDepartment().getMaxSalary()) <= 0) {
            employee.setSalary(salary);
            userService.updateUser(employee);
            return new ResponseEntity<User>(employee, HttpStatus.OK);
        } else {
            return new ResponseEntity(new CustomErrorType("Unable to update.Salaries are on not in range :" +
                    employee.getDepartment().getMinSalary() + " and" + employee.getDepartment().getMaxSalary()), HttpStatus.CONFLICT);
        }

    }


    @Secured("ROLE_HEAD")
    @PutMapping("/disable/{id}")
    public ResponseEntity<User> disable(@AuthenticationPrincipal UserDetails auth, @PathVariable Long id) {

        SetterLoginTime.setLoginTime(auth);
        User employee = userService.findUser(id);
        Department departmentOFHead = userService.findByEmail(auth.getUsername()).getDepartment();
        if (employee == null || !userService.findAllEmployeesOfDepartment(departmentOFHead.getId()).contains(employee)) {
            log.error("Unable to update. User with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Unable to update. User with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        employee.setActive(false);
        userService.updateUser(employee);
        return new ResponseEntity<User>(employee, HttpStatus.OK);
    }

    // -------------------Delete an Employee-------------------------------------------
    @Secured("ROLE_CEO")
    @DeleteMapping("/{id}")
    ResponseEntity deleteEmployeeByCeo(@PathVariable @Min(value = 1, message = "must be greater than or equal to 1") Long id, @AuthenticationPrincipal UserDetails auth) {
        SetterLoginTime.setLoginTime(auth);
        User employee = userService.findUser(id);
        if (employee == null) {
            log.error("Unable to update. User with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Unable to delete. Employee with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        userService.delete(employee);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Secured("ROLE_HEAD")
    @DeleteMapping("/departments/{id}")
    ResponseEntity deleteEmployeeByHead(@PathVariable @Min(value = 1, message = "must be greater than or equal to 1") Long id, @AuthenticationPrincipal UserDetails auth) {
        SetterLoginTime.setLoginTime(auth);
        User employee = userService.findUser(id);
        Department departmentOFHead = userService.findByEmail(auth.getUsername()).getDepartment();
        if (employee == null || !userService.findAllEmployeesOfDepartment(departmentOFHead.getId()).contains(employee)) {
            log.error("Unable to update. User with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Unable to delete. Employee with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        userService.delete(employee);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
