package task1.soft.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import task1.soft.api.entity.Departament;
import task1.soft.api.entity.Phone;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {
}
