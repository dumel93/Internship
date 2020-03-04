package task1.soft.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import task1.soft.api.entity.Department;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import task1.soft.api.entity.User;

import java.math.BigDecimal;


public interface DepartmentRepository extends JpaRepository<Department, Long>, CrudRepository<Department, Long> {

    Department save(Department department);

    Department findByCity(String city);

    Department findByName(String name);

    @Query("select avg(u.salary) from User u inner join u.department d where d.id=u.department.id and u.department.id= ?1")
    BigDecimal countAverageSalaries(Long idDepart);

    @Query("select u from User u inner join u.department d where d.id=u.department.id and u.department.id= ?1 and u.isHead=true ")
    User findHeadByIdDepart(Long idDepart);

}
