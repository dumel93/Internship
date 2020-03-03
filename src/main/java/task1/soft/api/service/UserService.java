package task1.soft.api.service;

import task1.soft.api.entity.Department;
import task1.soft.api.entity.User;


import java.math.BigDecimal;
import java.util.List;

public interface UserService {

    public void setupCEO();

    void findAllUsers();

    User findUser(Long id);

    List<User> findAllEmployeesOfDepartment(Long idDep);

    List<User> findAll();

    void save(User employee);

    void updateUser(User employee);

    void createRoles();

    void createEmployee(String firstName, String lastName, String email, String password, BigDecimal salary, Department department);

    void updatePassword(User emp, String newPassword);

    boolean isEmailExist(User employee);

    void delete(User employee);

    User findByEmail(String email);
}
