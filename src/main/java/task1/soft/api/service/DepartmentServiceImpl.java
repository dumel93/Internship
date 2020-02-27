package task1.soft.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task1.soft.api.entity.Department;
import task1.soft.api.entity.User;
import task1.soft.api.repo.DepartmentRepository;
import task1.soft.api.repo.UserRepository;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;


@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final UserRepository userRepository;

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(UserRepository userRepository, DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department createDepartment(String name, String city) {
        Department department = new Department();
        department.setName(name);
        department.setCity(city);

        List<User> employees=department.getEmployees();
        employees.addAll(userRepository.findAllEmployeesOfDepartment(department.getId()));
        department.setNumberOfEmployees(department.getEmployees().size());
        department.setAverageSalary( department.getEmployees().stream()
                .collect(Collectors.averagingDouble(User::getSalary)));
        department.setNumberOfEmployees(department.getEmployees().size());
        List<Double> doubleList=employees.stream().map(User::getSalary).collect(Collectors.toList());
        Double[] array = new Double[department.getEmployees().size()];
        doubleList.toArray(array);
        department.setMedianSalary(DoubleStream.of().sorted().toArray()[array.length / 2]);
        return department;

    }

    @Override
    public void save(Department department) {
        departmentRepository.save(department);
    }

}
