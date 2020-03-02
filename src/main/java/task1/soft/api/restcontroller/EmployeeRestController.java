package task1.soft.api.restcontroller;


import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import task1.soft.api.dto.EmployeeDTO;
import task1.soft.api.dto.EmployeePasswordDTO;
import task1.soft.api.dto.EmployeeSalaryDTO;
import task1.soft.api.entity.Department;
import task1.soft.api.entity.Role;
import task1.soft.api.entity.User;
import task1.soft.api.repo.DepartmentRepository;
import task1.soft.api.repo.RoleRepository;
import task1.soft.api.repo.UserRepository;
import task1.soft.api.service.UserService;
import task1.soft.api.util.CustomErrorType;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;


@RestController
@RequestMapping(value = "/employees", produces = "application/json")
public class EmployeeRestController {

    private final UserRepository userRepository;

    private final UserService userService;

    private final RoleRepository roleRepository;

    private final DepartmentRepository departmentRepository;

    private final ModelMapper modelMapper;
    public static final Logger logger = LoggerFactory.getLogger(EmployeeRestController.class);

    @Autowired
    public EmployeeRestController(UserRepository userRepository, UserService userService, RoleRepository roleRepository, DepartmentRepository departmentRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
    }

    @Secured({"ROLE_CEO", "ROLE_HEAD"})
    @GetMapping
    public ResponseEntity<List<User>> getAllEmployees() {

        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @Secured({"ROLE_CEO", "ROLE_HEAD"})
    @GetMapping("{/{id}")
    public ResponseEntity<User> getEmployeeById(@PathVariable Long id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            logger.error("User with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("User with id " + id
                    + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }


    @Secured({"ROLE_HEAD", "ROLE_EMPLOYEE"})
    @GetMapping("/departments")
    public ResponseEntity<List<User>> findAllEmployeesOfDepartment(@AuthenticationPrincipal UserDetails auth) {
        User currentUser = userRepository.findByEmail(auth.getUsername());
        currentUser.setLastLoginTime(new Date());
        userService.updateUser(currentUser);
        List<User> users = userService.findAllEmployeesOfDepartment(currentUser.getDepartment().getId());
        if (users.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);

    }

    @Secured({"ROLE_CEO", "ROLE_HEAD", "ROLE_EMPLOYEE"})
    @GetMapping("/departments/{id}")
    public ResponseEntity<List<User>> findEmployeesOfDepartment(@PathVariable Long id, @AuthenticationPrincipal UserDetails auth) {
        User currentUser = userRepository.findByEmail(auth.getUsername());
        currentUser.setLastLoginTime(new Date());
        userService.updateUser(currentUser);
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
    public ResponseEntity createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        User employee = modelMapper.map(employeeDTO, User.class);



        logger.info("Creating User : {}", employee);
        if (userService.isEmailExist(employee)) {
            logger.error("Unable to create. A User with email {} already exist", employee.getEmail());
           return new ResponseEntity(new CustomErrorType("Unable to create. A User with email " +
                   employee.getEmail() + " already exist."), HttpStatus.CONFLICT);
       }
        userService.createEmployee(employee.getFirstName(), employee.getLastName(), employee.getEmail(), employee.getPassword(), employee.getDepartment());
        return new ResponseEntity<String>(HttpStatus.CREATED);
    }

    @Secured("ROLE_CEO")
    @PutMapping("/head/{id}")
    public ResponseEntity<User> setHead(@PathVariable Long id) {
        User employee = userRepository.findOne(id);

        if (employee == null) {
            logger.error("Unable to update. User with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Unable to update. User with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }

        employee.setHead(true);
        Role userRole = roleRepository.findByName("ROLE_HEAD");
        employee.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        userService.updateUser(employee);
        return new ResponseEntity<User>(employee, HttpStatus.OK);
    }

    @Secured({"ROLE_CEO", "ROLE_HEAD"})
    @PostMapping("/createE")
    @ResponseStatus(HttpStatus.CREATED)
    public User createEmployeeInHeadDepartment(@AuthenticationPrincipal UserDetails auth, @RequestBody EmployeeDTO employeeDTO) {

        User employee = modelMapper.map(employeeDTO, User.class);
        employee.setFirstName(employee.getFirstName());
        employee.setLastName(employee.getLastName());
        employee.setActive(true);
        Role userRole = roleRepository.findByName("ROLE_EMPLOYEE");
        employee.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        Department department = userRepository.findByEmail(auth.getUsername()).getDepartment();
        department.getEmployees().add(employee);
        employee.setDepartment(department);
        departmentRepository.save(department);
        userRepository.save(employee);

        return employee;
    }

    @Secured("ROLE_HEAD")
    @PutMapping("/password/{id}")
    public ResponseEntity updatePassword(@AuthenticationPrincipal UserDetails auth, @RequestBody EmployeePasswordDTO employeePasswordDTO, @PathVariable Long id) {
        User employee = modelMapper.map(employeePasswordDTO, User.class);
        if (employee == null) {
            logger.error("Unable to update. User with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Unable to update. User with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        String newPassword = employee.getPassword();
        userService.updatePassword(employee, newPassword);
        return new ResponseEntity<User>(employee, HttpStatus.OK);
    }

    @Secured("ROLE_HEAD")
    @PutMapping("/salary/{id}")
    public ResponseEntity setSalary(@AuthenticationPrincipal UserDetails auth, @RequestBody EmployeeSalaryDTO employeeSalaryDTO, @PathVariable Long id) {


        User employee = modelMapper.map(employeeSalaryDTO, User.class);
        if (employee == null) {
            logger.error("Unable to update. User with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Unable to update. User with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        Double salary = employee.getSalary();
        if (salary >= employee.getDepartment().getMinSalary() && salary <= employee.getDepartment().getMaxSalary()) {
            employee.setSalary(salary);
            userService.updateUser(employee);
            return new ResponseEntity<User>(employee, HttpStatus.OK);
        } else {

            return new ResponseEntity(new CustomErrorType("Unable to update.Salaries are on not in range :"+employee.getDepartment().getMinSalary()+" and"+employee.getDepartment().getMaxSalary()),HttpStatus.NOT_FOUND);
        }

    }


    @Secured("ROLE_HEAD")
    @PutMapping("/disable/{id}")
    public void disable(@AuthenticationPrincipal UserDetails auth, @PathVariable Long id) {

        User emp = userRepository.findOne(id);
        if (userService.findAllEmployeesOfDepartment(userRepository.findByEmail(auth.getUsername()).getId()).contains(emp)) {
            emp.setActive(false);
            userService.updateUser(emp);

        } else {
            System.out.println("this head do not have employee in this department ");
        }
    }

}
