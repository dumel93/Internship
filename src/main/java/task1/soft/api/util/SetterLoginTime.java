package task1.soft.api.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import task1.soft.api.entity.User;
import task1.soft.api.repo.UserRepository;
import task1.soft.api.service.UserService;

import java.util.Date;

@Component
public class SetterLoginTime {

    private static UserRepository userRepository;
    private static UserService userService;

    @Autowired
    public SetterLoginTime(UserRepository userRepository, UserService userService) {
        SetterLoginTime.userRepository = userRepository;
        SetterLoginTime.userService = userService;
    }


    public static void setLoginTime(@AuthenticationPrincipal UserDetails auth) {
        User currentUser = userRepository.findByEmail(auth.getUsername());
        currentUser.setLastLoginTime(new Date());
        userService.updateUser(currentUser);
    }
}
