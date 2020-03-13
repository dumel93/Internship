package task1.soft.api.service;

import task1.soft.api.dto.EmployeeDTO;
import task1.soft.api.entity.Department;
import task1.soft.api.entity.User;
import java.util.List;

public interface UserService {

    User setupCEO();

    User findEmployee(Long id);

    List<User> findAllEmployeesOfDepartment(Long idDep);

    List<User> findAllEmployees();

    void updateEmployee(EmployeeDTO EmployeeDTO);

    void createRoles();

    User createEmployee(EmployeeDTO employeeDTO);

    void deleteEmployee(User employee);

    User findByEmail(String email);

    void setLoginTime(Long userId);

    void setSalary(User employee);

    boolean isUserInHeadDepart(User user, Department headDepartment);

}
