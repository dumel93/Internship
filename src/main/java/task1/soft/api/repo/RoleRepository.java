package task1.soft.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import task1.soft.api.entity.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

}