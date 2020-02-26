package task1.soft.api.restcontroller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.RestController;
import task1.soft.api.repo.RoleRepository;
import task1.soft.api.repo.UserRepository;
import task1.soft.api.service.UserService;

@RestController
public class Hello {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private UserRepository userRepository;



    @EventListener(ApplicationReadyEvent.class)
    public void setUpCEO() {

       if(userRepository.findAll().size()==0){
           userService.saveCEO();
        }

    }
}
