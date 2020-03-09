package task1.soft.api.service;

import task1.soft.api.dto.DepartmentDTO;
import task1.soft.api.entity.Department;
import task1.soft.api.util.SearchCriteria;

import java.math.BigDecimal;
import java.util.List;


public interface DepartmentService {

    Department createDepartment(String name, String city);

    Department updateDepartment(Department department);

    Department findOne(Long id);

    void delete(Long idDepart);

    DepartmentDTO setEmployeesDetails(Long idDepart);

    List<Department> findAll(Integer offset, Integer limit, String sortBy, String orderBy);

    List<Department> findAll();

    BigDecimal calculateMedian(Long idDepart);

    List<Department> searchDepartment(List<SearchCriteria> params);


}
