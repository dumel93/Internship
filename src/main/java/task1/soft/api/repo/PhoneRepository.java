package task1.soft.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import task1.soft.api.entity.Phone;


public interface PhoneRepository extends JpaRepository<Phone, Long> {
}
