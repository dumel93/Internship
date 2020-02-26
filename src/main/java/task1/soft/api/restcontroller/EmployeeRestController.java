package task1.soft.api.restcontroller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

        return departmentRepository.findAll();
//
    }
}
