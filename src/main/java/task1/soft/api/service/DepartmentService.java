package task1.soft.api.service;

import task1.soft.api.entity.Department;

public interface DepartmentService {

    Department createDepartment(String name, String city);
    void save(Department department);
}
