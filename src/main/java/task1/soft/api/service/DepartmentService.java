package task1.soft.api.service;

import task1.soft.api.dto.DepartmentDTO;
import task1.soft.api.entity.Department;
import task1.soft.api.entity.User;
import task1.soft.api.exception.NotFoundException;
import java.math.BigDecimal;
import java.util.List;


public interface DepartmentService {

    Department createDepartment(DepartmentDTO departmentDTO);

    Department updateDepartment(DepartmentDTO departmentDTO);

    Department findDepartment(Long id);

    void deleteDepartment(Long idDepart);

    DepartmentDTO getAllDepartmentDetails(Long idDepart);

    List<Department> findAllDepartments();

    BigDecimal calculateMedian(Long idDepart);

    User findHeadByIdDepart(Long idDepart);


}
