package task1.soft.api.restcontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import task1.soft.api.dto.EmployeeDTO;
import task1.soft.api.dto.EmployeePasswordDTO;
import task1.soft.api.dto.EmployeeReadDTO;
import task1.soft.api.dto.EmployeeSalaryDTO;
import task1.soft.api.entity.Department;
import task1.soft.api.entity.User;
import task1.soft.api.exception.UserExistsException;
import task1.soft.api.service.DepartmentService;
import task1.soft.api.service.UserService;
import task1.soft.api.exception.NotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/employees", produces = "application/json")
public class EmployeeRestController {


    private final UserService userService;
    private final DepartmentService departmentService;
    private final ModelMapper modelMapper;

    // -------------------Get an Employee/s-------------------------------------------
    @Secured({"ROLE_CEO", "ROLE_HEAD"})
    @GetMapping
    public List<EmployeeReadDTO> getAllEmployees(@AuthenticationPrincipal UserDetails auth

                                                 ) {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        List<User> employees = userService.findAll();

        return employees.stream()
                .map(employee -> modelMapper.map(employee, EmployeeReadDTO.class))
                .collect(Collectors.toList());
    }

    @Secured({"ROLE_CEO", "ROLE_HEAD"})
    @GetMapping("/{id}")
    public EmployeeReadDTO getEmployeeById(@PathVariable Long id, @AuthenticationPrincipal UserDetails auth) {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User user = userService.findUser(id);

        return modelMapper.map(user, EmployeeReadDTO.class);
    }


    @Secured({"ROLE_HEAD", "ROLE_EMPLOYEE"})
    @GetMapping("/departments")
    public List<EmployeeReadDTO> findAllEmployeesOfDepartment(@AuthenticationPrincipal UserDetails auth) {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User currentUser = userService.findByEmail(auth.getUsername());
        Department department = currentUser.getDepartment();
        List<User> employees = userService.findAllEmployeesOfDepartment(department.getId());
        return employees.stream()
                .map(entity -> modelMapper.map(entity, EmployeeReadDTO.class))
                .collect(Collectors.toList());


    }

    @Secured({"ROLE_CEO", "ROLE_HEAD"})
    @GetMapping("/departments/{id}")
    public List<EmployeeReadDTO> findEmployeesOfDepartment(@PathVariable Long id, @AuthenticationPrincipal UserDetails auth) {

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        List<User> employees = userService.findAllEmployeesOfDepartment(id);
        return employees.stream()
                .map(entity -> modelMapper.map(entity, EmployeeReadDTO.class))
                .collect(Collectors.toList());
    }

    // -------------------Create an Employee-------------------------------------------
    @Secured("ROLE_CEO")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    public EmployeeReadDTO createEmployee(@AuthenticationPrincipal UserDetails auth, @Valid @RequestBody EmployeeDTO employeeDTO) throws NotFoundException, UserExistsException {
        User employee = modelMapper.map(employeeDTO, User.class);
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        log.info("Creating User : {}", employee);
        if (userService.isEmailExist(employee)) {
            log.error("Unable to create. A User with email {} already exist", employee.getEmail());

        }

        Department department = departmentService.findOne(employee.getDepartment().getId());
        userService.createEmployee(employee.getFirstName(), employee.getLastName(), employee.getEmail(), employee.getPassword(), employee.getSalary(), department);
        return modelMapper.map(employee, EmployeeReadDTO.class);
    }

    @Secured("ROLE_HEAD")
    @PostMapping("/departments")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeReadDTO createEmployeeInHeadDepartment(@AuthenticationPrincipal UserDetails auth, @Valid @RequestBody EmployeeDTO employeeDTO) throws UserExistsException {

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User employee = modelMapper.map(employeeDTO, User.class);

        if (userService.isEmailExist(employee)) {
            log.error("Unable to create. A User with email {} already exist", employee.getEmail());

        }
        Department department = userService.findByEmail(auth.getUsername()).getDepartment();
        department.getEmployees().add(employee);
        departmentService.updateDepartment(department);
        employee.setDepartment(department);
        userService.createEmployee(employee.getFirstName(), employee.getLastName(), employee.getEmail(), employee.getPassword(), employee.getSalary(), employee.getDepartment());
        return modelMapper.map(employee, EmployeeReadDTO.class);

    }

    // -------------------Update an Employee-------------------------------------------

    @Secured("ROLE_HEAD")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    void updateEmployee(@AuthenticationPrincipal UserDetails auth, @Valid @RequestBody EmployeeDTO employeePasswordDTO, @PathVariable Long id) {

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User employee = modelMapper.map(employeePasswordDTO, User.class);
        employee.setId(id);
        userService.updateUser(employee);
    }

    @Secured("ROLE_CEO")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/departments/head/{id}")
    public void setHead(@PathVariable Long id, @AuthenticationPrincipal UserDetails auth) {

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User employee = userService.findUser(id);
        User head = departmentService.findHeadByIdDepart(employee.getDepartment().getId());
        if (head == null) {
            employee.setHead(true);
            userService.updateUser(employee);
        } else {
            head.setHead(false);
            employee.setHead(true);
            userService.updateUser(employee);
            userService.updateUser(head);

        }

    }


    @Secured("ROLE_HEAD")
    @PutMapping("password/{id}/")
    @ResponseStatus(HttpStatus.OK)
    public void updatePassword(@AuthenticationPrincipal UserDetails auth, @Valid @RequestBody EmployeePasswordDTO employeePasswordDTO, @PathVariable Long id) {

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User employee = userService.findUser(id);
        Department departmentOFHead = userService.findByEmail(auth.getUsername()).getDepartment();
        if (employee != null || !userService.findAllEmployeesOfDepartment(departmentOFHead.getId()).contains(employee)) {
            String newPassword = employeePasswordDTO.getPassword();
            userService.updatePassword(employee, newPassword);
        }
    }

    @Secured("ROLE_HEAD")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/salary/{id}")
    public void setSalary(@AuthenticationPrincipal UserDetails auth, @Valid @RequestBody EmployeeSalaryDTO employeeSalaryDTO, @PathVariable Long id) {

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User employee = modelMapper.map(employeeSalaryDTO, User.class);
        Long departmentId = userService.findByEmail(auth.getUsername()).getDepartment().getId();
        if (employee == null || !userService.findAllEmployeesOfDepartment(departmentId).contains(employee)) {
            log.error("Unable to update. User with id {} not found.", id);

        }

        assert employee != null;
        BigDecimal salary = employee.getSalary();
        if (salary.compareTo(employee.getDepartment().getMinSalary()) >= 0 && salary.compareTo(employee.getDepartment().getMaxSalary()) <= 0) {
            employee.setSalary(salary);

        } else { // automatically adjusted (to max)
            employee.setSalary(employee.getDepartment().getMaxSalary());
        }
        userService.updateUser(employee);

    }


    @Secured("ROLE_HEAD")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/disable/{id}")
    public void disable(@AuthenticationPrincipal UserDetails auth, @PathVariable Long id) {

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User employee = userService.findUser(id);
        Department departmentOFHead = userService.findByEmail(auth.getUsername()).getDepartment();
        if (employee == null || !userService.findAllEmployeesOfDepartment(departmentOFHead.getId()).contains(employee)) {
            log.error("Unable to update. User with id {} not found.", id);

        } else {
            employee.setActive(false);
            userService.updateUser(employee);
        }

    }

    // -------------------Delete an Employee-------------------------------------------
    @Secured("ROLE_HEAD")
    @DeleteMapping("/{id}")
    public void deleteEmployeeByCeo(@PathVariable @Min(value = 1, message = "must be greater than or equal to 1") Long id,
                                    @AuthenticationPrincipal UserDetails auth,
                                    SecurityContextHolderAwareRequestWrapper request) {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User employee = userService.findUser(id);
        User currentUser = userService.findByEmail(auth.getUsername());
        if (request.isUserInRole("ROLE_CEO")) {
            if (employee != null) userService.delete(employee);
        }
        if (request.isUserInRole("ROLE_HEAD")) {
            if (employee == null || !userService.findAllEmployeesOfDepartment(currentUser.getDepartment().getId()).contains(employee)) {
                log.error("Unable to delete. User with id {} not found.", id);
                log.error("Unable to delete. User with id {} not found in department of head: {}.", id, currentUser.getDepartment().getName());
            } else {
                userService.delete(employee);
            }
        }

    }

}
