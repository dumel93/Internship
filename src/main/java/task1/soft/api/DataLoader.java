package task1.soft.api;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import task1.soft.api.service.UserService;


@RequiredArgsConstructor
@Component
public class DataLoader {

    private final UserService userService;

    @EventListener(ApplicationReadyEvent.class)
    public void setUpDB() {

        if (userService.findAll().isEmpty()) {
            userService.createRoles();
            userService.setupCEO();

        }
    }
}
