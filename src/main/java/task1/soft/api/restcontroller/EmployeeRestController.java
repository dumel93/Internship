package task1.soft.api.restcontroller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import task1.soft.api.entity.Departament;
import task1.soft.api.entity.Role;
import task1.soft.api.entity.User;
import task1.soft.api.repo.RoleRepository;
import task1.soft.api.repo.UserRepository;
import task1.soft.api.service.UserService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping(value ="/employees",produces = "application/json")
public class EmployeeRestController {

    private  final UserRepository userRepository;

    private  final UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    public EmployeeRestController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @GetMapping("/")
    public List<User> hello(){

        return userService.findAll();
//
    }

    public  Long findIdOfHead(@AuthenticationPrincipal  UserDetails auth){

        String email= auth.getUsername();
        User head= userRepository.findByEmail(email);
        return head.getDepartament().getId();

    }

    @Secured("ROLE_HEAD")
    @GetMapping("/departments")
    public List<User> hello2(@AuthenticationPrincipal  UserDetails auth){

        return userService.findAllEmployyesOfDepForHead(this.findIdOfHead(auth));
//
    }

    @Secured("ROLE_CEO")
    @GetMapping("/departments/{id}")
    public List<User> get3( @PathVariable Long id){
        return userService.findAllEmployyesOfDepForCEO(id);
    }

    @Secured("ROLE_CEO")
    @PostMapping("/")
    public User create( @RequestBody User employee){
        userService.save(employee);
        return employee;
    }

    @Secured("ROLE_CEO")
    @PutMapping("/{id}/head")
    public void setHead(@PathVariable Long id){
        User employee= userRepository.findOne(id);
        employee.setHead(true);
        Role userRole = roleRepository.findByName("ROLE_HEAD");
        employee.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        employee.setUserId(id);
        userService.save(employee);

    }

    @Secured("ROLE_HEAD")
    @PutMapping("/change_pass/{id}")
    public void  updatePassword(@AuthenticationPrincipal  UserDetails auth,@RequestBody User employee, @PathVariable Long id) {

        User emp=userRepository.findOne(id);

        String newPass=employee.getPassword();
        emp.setPassword(passwordEncoder.encode(newPass));
        userService.update(emp,  id);


    }









}
