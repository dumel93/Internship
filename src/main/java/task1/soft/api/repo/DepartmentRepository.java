package task1.soft.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import task1.soft.api.entity.Departament;
import task1.soft.api.entity.Role;

@Repository
public interface DepartmentRepository extends JpaRepository<Departament, Long> {

}
