package task1.soft.api.service;

import task1.soft.api.entity.Department;
import task1.soft.api.util.DTOEntity;

public interface DepartmentService {

    Department createDepartment(String name, String city);

    void saveDepartment(Department department);

    void updateDepartment(Department department);
}
