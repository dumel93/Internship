package task1.soft.api.service;

import task1.soft.api.dto.EmployeeDTO;
import task1.soft.api.dto.EmployeeReadDTO;
import task1.soft.api.entity.Department;
import task1.soft.api.entity.User;
import task1.soft.api.validation.FieldValueExists;

import java.util.List;

public interface UserService extends FieldValueExists {

    User setupCEO();

    User findEmployee(Long id);

    List<User> findAllEmployeesOfDepartment(Long idDep);

    List<User> findAllEmployees();

    User updateEmployee(EmployeeDTO EmployeeDTO);

    void createRoles();

    User createEmployee(EmployeeDTO employeeDTO);

    void deleteEmployee(User employee);

    User findByEmail(String email);

    void setLoginTime(Long userId);

    void setSalary(User employee);

    boolean isUserInHeadDepart(User user, Department headDepartment);

    EmployeeReadDTO getDataFromEmployeeReadDTO(EmployeeReadDTO employee);

    void setHead(EmployeeDTO employeeDTO, User user, Department department);

    void setActivity(User employee);

    void setPhones(EmployeeDTO employeeDTO, User user);
}

