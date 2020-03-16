package task1.soft.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import task1.soft.api.entity.Department;
import task1.soft.api.entity.User;
import java.math.BigDecimal;


public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("select avg(u.salary) from User u where u.department.id= ?1")
    BigDecimal countAverageSalaries(Long idDepart);

    @Query("select u from User u where u.department.id = ?1 and u.isHead = true")
    User findHeadByIdDepart(Long idDepart);

    @Query("select count(u) from User u where u.department.id= ?1 ")
    int countEmployeesByIdDepart(Long idDepart);

}
