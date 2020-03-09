package task1.soft.api;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import task1.soft.api.service.UserService;


@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class DataLoader {

    private final UserService userService;

    @EventListener(ApplicationReadyEvent.class)
    public void setUpDB() {

        if (userService.findAll().size() == 0) {
            userService.createRoles();
            userService.setupCEO();

        }
    }
}
