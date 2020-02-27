package task1.soft.api.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import task1.soft.api.entity.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);


    @Query(value = "select u from User u where u.departament.id= ?1")
    List<User> findAllEmployyesOfDep(Long idDep);
}
