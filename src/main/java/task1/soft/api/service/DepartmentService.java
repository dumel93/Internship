package task1.soft.api.service;

import task1.soft.api.entity.Department;

import java.util.List;


public interface DepartmentService {

    Department createDepartment(String name, String city);

    void saveDepartment(Department department);

    void updateDepartment(Department department);

    List<Department> findAll();

    Department findOne(Long id);

    void delete(Department department);
}
