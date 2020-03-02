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
//
//    @Override
//    public DTOEntity createDepartment(String name, String city) {
//        Department department = readDepartment(name, city);
////        List<Double> doubleList=employees.stream().map(User::getSalary).collect(Collectors.toList());
////        Double[] array = new Double[department.getEmployees().size()];
////        doubleList.toArray(array);
//        departmentRepository.save(department);
//        new DtoUtils(modelMapper);
//        return DtoUtils.convertToDto(department, new DepartmentDTO());




//        department.setMedianSalary(DoubleStream.of().sorted().toArray()[array.length / 2]);


//    }


    public Department createDepartment(String name, String city) {
        Department department = new Department();
        department.setName(name);
        department.setCity(city);
        List<User> employees= userRepository.findAllEmployeesOfDepartment(department.getId());
        department.setEmployees(employees);
        department.setNumberOfEmployees(department.getEmployees().size());
        department.setAverageSalary( department.getEmployees().stream()
                .collect(Collectors.averagingDouble(User::getSalary)));
        department.setNumberOfEmployees(department.getEmployees().size());
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
        department.setEmployees(department.getEmployees());
        department.setName(department.getName());
        department.setCity(department.getCity());
        department.setMaxSalary(department.getMaxSalary());
        department.setMinSalary(department.getMinSalary());
        department.setNumberOfEmployees(department.getNumberOfEmployees());
        department.setAverageSalary(department.getAverageSalary());
        department.setMedianSalary(department.getMedianSalary());
        departmentRepository.save(department);
    }


}
