package task1.soft.api.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import task1.soft.api.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
