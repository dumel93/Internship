package task1.soft.api.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task1.soft.api.entity.Department;
import task1.soft.api.entity.User;
import task1.soft.api.repo.DepartmentRepository;
import task1.soft.api.repo.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(UserRepository userRepository, DepartmentRepository departmentRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    public Department createDepartment(String name, String city) {
        Department department = new Department();
        department.setName(name);
        department.setCity(city);
        setEmployeesData(department);
        departmentRepository.save(department);
        return department;
    }

    @Override
    public void saveDepartment(Department department) {
        departmentRepository.save(department);
    }

    @Override
    public void updateDepartment(Department department) {

        department.setId(department.getId());
        department.setName(department.getName());
        department.setCity(department.getCity());
        setEmployeesData(department);
        departmentRepository.save(department);
    }

    private void setEmployeesData(Department department) {
        List<User> employees = userRepository.findAllEmployeesOfDepartment(department.getId());
        department.setEmployees(employees);
        department.setNumberOfEmployees(department.getEmployees().size());
        department.setAverageSalary(department.getEmployees().stream()
                .collect(Collectors.averagingDouble(User::getSalary)));
        department.setNumberOfEmployees(department.getEmployees().size());
        department.setMedianSalary(department.getMedianSalary());
        //List<Double> doubleList=employees.stream().map(User::getSalary).collect(Collectors.toList());
////        Double[] array = new Double[department.getEmployees().size()];
////        doubleList.toArray(array);
//        departmentRepository.save(department);
//        new DtoUtils(modelMapper);
//        return DtoUtils.convertToDto(department, new DepartmentDTO());
//        department.setMedianSalary(DoubleStream.of().sorted().toArray()[array.length / 2]);
    }

}
