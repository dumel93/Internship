package task1.soft.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import task1.soft.api.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Query(value = "select u from User u where u.department.id = ?1 ")
    List<User> findAllEmployeesOfDepartment(Long idDepart);

    @Modifying
    @Query("update User u set u.lastLoginTime = current_timestamp where u.id = ?1 ")
    void setLoginTime(Long userId);

    boolean existsByEmail(String email);


}
