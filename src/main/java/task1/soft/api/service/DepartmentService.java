package task1.soft.api.service;

import task1.soft.api.dto.DepartmentDTO;
import task1.soft.api.entity.Department;

import java.util.List;


public interface DepartmentService {

    Department createDepartment(String name, String city);

    Department updateDepartment(Department department);

    Department findOne(Long id);

    void delete(Department department);

    DepartmentDTO setEmployeesDetails(Long idDepart);

    List<Department> findAll(Integer offset, Integer limit, String sortBy, String orderBy);
}
