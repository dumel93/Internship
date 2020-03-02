package task1.soft.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import task1.soft.api.repo.DepartmentRepository;
import task1.soft.api.service.DepartmentService;
import task1.soft.api.service.UserService;

@Component
public class DataLoader {

    private final UserService userService;

    private final DepartmentService departmentService;

    private DepartmentRepository departmentRepository;

    @Autowired
    public DataLoader(UserService userService, DepartmentService departmentService, DepartmentRepository departmentRepository) {
        this.userService = userService;
        this.departmentService = departmentService;
        this.departmentRepository = departmentRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void setUpDB() {

        if (userService.findAll().size() == 0) {
            userService.createRoles(); // create 3 roles and ceo..
            userService.setupCEO();
            departmentService.createDepartment("it","rzeszow");

        }
//
//                    userService.createEmployee("damian","krawczyk","wwww2@wp.pl","user123", departmentRepository.findOne(1L));
    }
}
