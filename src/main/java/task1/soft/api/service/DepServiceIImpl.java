package task1.soft.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task1.soft.api.entity.Departament;
import task1.soft.api.repo.DepartmentRepository;
import task1.soft.api.repo.UserRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class DepServiceIImpl implements DepartmentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void savedep() {
        Departament departament = new Departament();
        departament.setName("it");
        departament.setCity("rzeszow");
        departament.setAverageSalary(100f);
        departament.setHeadOfDepartament(userRepository.findByEmail("ceo@pgs.com"));
        departament.setNumberOfEmployyes(10);
        departament.setMaxSalary(200f);
        departament.setMinSalary(50f);
        departament.setNumberOfEmployyes(1);
        departmentRepository.save(departament);
    }
}
