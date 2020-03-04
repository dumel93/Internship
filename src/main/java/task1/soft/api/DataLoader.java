package task1.soft.api;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import task1.soft.api.repo.DepartmentRepository;
import task1.soft.api.service.DepartmentService;
import task1.soft.api.service.UserService;

import java.math.BigDecimal;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class DataLoader {

    private final UserService userService;
    private DepartmentService departmentService;


    @EventListener(ApplicationReadyEvent.class)
    public void setUpDB() {

        if (userService.findAll().size() == 0) {
            userService.createRoles();
            userService.setupCEO();
//            departmentService.createDepartment("it","rzeszow");

        }
//                    userService.createEmployee("damian","krawczyk","wwww2@wp.pl","user123", new BigDecimal("2000"), departmentService.findOne(1L));
    }
}
