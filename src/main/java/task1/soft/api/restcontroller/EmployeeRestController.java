package task1.soft.api.restcontroller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping(value = "/employees", produces = "application/json")
public class EmployeeRestController {

    private final UserRepository userRepository;

    private final UserService userService;

    private final RoleRepository roleRepository;

    private final DepartmentRepository departmentRepository;

    @Autowired
    public EmployeeRestController(UserRepository userRepository, UserService userService, RoleRepository roleRepository, DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }


    @Secured("ROLE_HEAD")
    @GetMapping("/departments")
    public List<User> findAllEmployeesOfDepartment(@AuthenticationPrincipal UserDetails auth) {

        return userService.findAllEmployeesOfDepartment(userRepository.findByEmail(auth.getUsername()).getId());

    }

    @Secured({"ROLE_CEO", "ROLE_HEAD"})
    @GetMapping("/departments/{id}")
    public List<User> findAllEmployeesOfDepartment(@PathVariable Long id) {
        return userService.findAllEmployeesOfDepartment(id);
    }

    @Secured("ROLE_CEO")
    @PostMapping("/")
    public User createEmployee(@RequestBody User employee) {
        userService.save(employee);
        return employee;
    }

    @Secured("ROLE_CEO")
    @PutMapping("/head/{id}")
    public void setHead(@PathVariable Long id) {
        User employee = userRepository.findOne(id);
        employee.setHead(true);
        Role userRole = roleRepository.findByName("ROLE_HEAD");
        employee.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        employee.setId(id);

        userService.save(employee);
    }

    @Secured({"ROLE_CEO", "ROLE_HEAD"})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createEmployeeInHeadDepartment(@AuthenticationPrincipal UserDetails auth, @RequestBody EmployeeDTO employee) {

        User newEmployee = new User();
        newEmployee.setFirstName(employee.getFirstName());
        newEmployee.setLastName(employee.getLastName());
        newEmployee.setActive(true);
        Role userRole = roleRepository.findByName("ROLE_EMPLOYEE");
        newEmployee.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        Department department = userRepository.findByEmail(auth.getUsername()).getDepartment();
        newEmployee.setDepartment(departmentRepository.findOne(department.getId()));
        userRepository.save(newEmployee);

        return newEmployee;

    }

    @Secured("ROLE_HEAD")
    @PutMapping("/password/{id}")
    public void updatePassword(@AuthenticationPrincipal UserDetails auth, @RequestBody EmployeePasswordDTO employee, @PathVariable Long id) {

        User emp = userRepository.findOne(id);
        String newPassword = employee.getPassword();
        userService.updatePassword(emp,id,newPassword);
    }

    @Secured("ROLE_HEAD")
    @PutMapping("/salary/{id}")
    public void setSalary(@AuthenticationPrincipal UserDetails auth, @RequestBody EmployeeSalaryDTO employee, @PathVariable Long id) {


        User emp = userRepository.findOne(id);
        if (!userService.findAllEmployeesOfDepartment(userRepository.findByEmail(auth.getUsername()).getId()).contains(emp)) {
            System.out.println("this head do not have employee in this department ");
        } else {
            Double salary = employee.getSalary();

            if (salary >= emp.getDepartment().getMinSalary() && salary <= emp.getDepartment().getMaxSalary()) {
                emp.setSalary(salary);
                userService.updateUser(emp, id);
            } else {
                System.out.printf("wrong amount");
            }
        }
    }


    @Secured("ROLE_HEAD")
    @PutMapping("/disable/{id}")
    public void disable(@AuthenticationPrincipal UserDetails auth, @PathVariable Long id) {

        User emp = userRepository.findOne(id);
        if (userService.findAllEmployeesOfDepartment(userRepository.findByEmail(auth.getUsername()).getId()).contains(emp)) {
            emp.setActive(false);
            userService.updateUser(emp, id);

        } else {
            System.out.println("this head do not have employee in this department ");
        }
    }

}
