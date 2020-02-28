package task1.soft.api.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task1.soft.api.dto.DepartmentDTO;
import task1.soft.api.entity.Department;
import task1.soft.api.entity.User;
import task1.soft.api.repo.DepartmentRepository;
import task1.soft.api.repo.UserRepository;
import task1.soft.api.util.DTOEntity;
import task1.soft.api.util.DtoUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final UserRepository userRepository;

    private final DepartmentRepository departmentRepository;
    private  final ModelMapper modelMapper;

    @Autowired
    public DepartmentServiceImpl(UserRepository userRepository, DepartmentRepository departmentRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public DTOEntity createDepartment(String name, String city) {
        Department department = readDepartment(name, city);
//        List<Double> doubleList=employees.stream().map(User::getSalary).collect(Collectors.toList());
//        Double[] array = new Double[department.getEmployees().size()];
//        doubleList.toArray(array);
        departmentRepository.save(department);
        return new DtoUtils(modelMapper).convertToDto(department, new DepartmentDTO());




//        department.setMedianSalary(DoubleStream.of().sorted().toArray()[array.length / 2]);


    }

    private Department readDepartment(String name, String city) {
        Department department = new Department();
        department.setName(name);
        department.setCity(city);
        List<User> employees=department.getEmployees();
        employees.addAll(userRepository.findAllEmployeesOfDepartment(department.getId()));
        department.setNumberOfEmployees(department.getEmployees().size());
        department.setAverageSalary( department.getEmployees().stream()
                .collect(Collectors.averagingDouble(User::getSalary)));
        department.setNumberOfEmployees(department.getEmployees().size());
        return department;
    }

    @Override
    public void saveDepartment(Department department) {
        Department department1= this.readDepartment(department.getName(),department.getCity());
        departmentRepository.save(department1);
    }

    @Override
    public void updateDepartment(Department department) {

        Department department1= (Department) this.createDepartment(department.getName(),department.getCity());
        department1.setId(department.getId());
        departmentRepository.save(department1);
    }


}
