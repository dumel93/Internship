package task1.soft.api.service;

import task1.soft.api.dto.EmployeeDTO;
import task1.soft.api.entity.User;
import java.util.List;

public interface UserService {

    void setupCEO();

    User findUser(Long id);

    List<User> findAllEmployeesOfDepartment(Long idDep);

    List<User> findAll();

    void updateUser(User employee);

    void createRoles();

    void createEmployee(EmployeeDTO employeeDTO);

    void updatePassword(User employee, String newPassword);

    void delete(User employee);

    User findByEmail(String email);

    void setLoginTime(Long userId);

    void setSalary(User employee);

}
