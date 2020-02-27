package task1.soft.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import task1.soft.api.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Department save(Department department);
}
