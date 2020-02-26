package task1.soft.api.restcontroller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import task1.soft.api.entity.User;
import task1.soft.api.repo.UserRepository;
import task1.soft.api.service.UserService;

import java.util.List;

@RestController
@RequestMapping(value ="/employees",produces = "application/json")
public class EmployeeRestController {

    private  final UserRepository userRepository;

    private  final UserService userService;

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

    @Secured("ROLE_HEAD")
    @GetMapping("/departments")
    public List<User> hello2(@AuthenticationPrincipal  UserDetails auth){

        String email= auth.getUsername();
        User head= userRepository.findByEmail(email);
        Long idDep= head.getDepartaments().getId();

        return userService.findAllEmployyesOfDep(idDep);
//
    }

    @Secured("ROLE_CEO")
    @GetMapping("/departments/{id}")
    public List<User> get3( @PathVariable Long id){
        return userService.findAllEmployyesOfDep(id);
    }

    @Secured("ROLE_CEO")
    @PostMapping("/")
    public User create( @RequestBody User employee){
        userService.save(employee);
        return employee;
    }







}
