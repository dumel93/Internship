package task1.soft.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import task1.soft.api.repo.PhoneRepository;
import task1.soft.api.service.DepartmentService;
import task1.soft.api.service.UserService;

@Component
public class DataLoader {

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentService departmentService;




    @EventListener(ApplicationReadyEvent.class)
    public void setUpDB() {


            if(userService.findAll().size()==0){
                userService.createRoles(); // create 3 roles
                userService.saveCEO(); //custom ceo
                departmentService.savedep(); // custom department
                userService.saveEmployee(); // custom employee
            }


    }


}
