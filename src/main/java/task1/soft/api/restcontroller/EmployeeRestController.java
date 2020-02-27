package task1.soft.api.restcontroller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import task1.soft.api.entity.department;
import task1.soft.api.entity.Role;
import task1.soft.api.entity.User;
import task1.soft.api.repo.DepartmentRepository;
import task1.soft.api.repo.RoleRepository;
import task1.soft.api.repo.UserRepository;
import task1.soft.api.service.UserService;

import javax.validation.constraints.Min;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping(value = "/employees", produces = "application/json")
public class EmployeeRestController {

    private final UserRepository userRepository;

    private final UserService userService;

    private final BCryptPasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final DepartmentRepository departmentRepository;

    @Autowired
    public EmployeeRestController(UserRepository userRepository, UserService userService, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository, DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    public Long findIdOfHead(@AuthenticationPrincipal UserDetails auth) {

        String email = auth.getUsername();
        User head = userRepository.findByEmail(email);
        return head.getdepartment().getId();

    }

    @Secured("ROLE_HEAD")
    @GetMapping("/departments")
    public List<User> hello2(@AuthenticationPrincipal UserDetails auth) {

        return userService.findAllEmployyesOfDepForHead(this.findIdOfHead(auth));

    }

    @Secured("ROLE_CEO")
    @GetMapping("/departments/{id}")
    public List<User> get3(@PathVariable Long id) {
        return userService.findAllEmployyesOfDepForCEO(id);
    }

    @Secured("ROLE_CEO")
    @PostMapping("/")
    public User create(@RequestBody User employee) {
        userService.save(employee);
        return employee;
    }

    @Secured("ROLE_CEO")
    @PutMapping("/{id}/head")
    public void setHead(@PathVariable Long id) {
        User employee = userRepository.findOne(id);
        employee.setHead(true);
        Role userRole = roleRepository.findByName("ROLE_HEAD");
        employee.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        employee.setUserId(id);
        userService.save(employee);

    }


    @Secured("ROLE_HEAD")
    @PostMapping("/create_employee")
    @ResponseStatus(HttpStatus.CREATED)
    public User add2(@AuthenticationPrincipal UserDetails auth, @RequestBody User employee) {

        User newEmployee = new User();
        newEmployee.setFirstName(employee.getFirstName());
        newEmployee.setLastName(employee.getLastName());
        newEmployee.setPassword(passwordEncoder.encode(employee.getPassword()));
        newEmployee.setActive(true);
        Role userRole = roleRepository.findByName("ROLE_EMPLOYEE");
        newEmployee.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        department department = userRepository.findByEmail(auth.getUsername()).getdepartment();
        newEmployee.setdepartment(departmentRepository.findOne(department.getId()));
        userRepository.save(employee);

        return newEmployee;

    }


    @Secured("ROLE_HEAD")
    @PutMapping("/change_pass/{id}")
    public void updatePassword(@AuthenticationPrincipal UserDetails auth, @RequestBody User employee, @PathVariable Long id) {

        User emp = userRepository.findOne(id);

        String newPass = employee.getPassword();
        emp.setPassword(passwordEncoder.encode(newPass));
        userService.update(emp, id);


    }


    @Secured("ROLE_HEAD")
    @PutMapping("/setSalary/{id}")
    public void setSalary(@AuthenticationPrincipal UserDetails auth, @RequestBody User employee, @PathVariable Long id) {


        User emp = userRepository.findOne(id);
        if (!userService.findAllEmployyesOfDepForHead(this.findIdOfHead(auth)).contains(emp)) {
            System.out.println("this head do not have employee in this department ");
        } else {
            Float salary = employee.getSalary();

            if (salary >= emp.getdepartment().getMinSalary() && salary <= emp.getdepartment().getMaxSalary()) {
                emp.setSalary(salary);
                userService.update(emp, id);
            } else {
                System.out.printf("wrong amount");
            }
        }


    }


    @Secured("ROLE_HEAD")
    @PutMapping("/disable/{id}")
    public void disable(@AuthenticationPrincipal UserDetails auth, @PathVariable Long id) {

        User emp = userRepository.findOne(id);
        if (userService.findAllEmployyesOfDepForHead(this.findIdOfHead(auth)).contains(emp)) {
            emp.setActive(false);
            userService.update(emp, id);

        } else {
            System.out.println("this head do not have employee in this department ");
        }


    }


}
