package task1.soft.api.service;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import task1.soft.api.entity.User;

import java.util.List;

public interface UserService {

    public void setupCEO();

    void findAllUsers();

    List<User> findAllEmployeesOfDepartment(Long idDep);

    List<User> findAll();

    void save(User employee);


    void updateUser(User employee);

    void createRoles();

    void createEmployee(String firstName, String lastName, String email, String password);


    void updatePassword(User emp, String newPassword);
}
