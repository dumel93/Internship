package task1.soft.api.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import task1.soft.api.dto.DepartmentDTO;
import task1.soft.api.dto.EmployeeReadDTO;
import task1.soft.api.entity.Department;
import task1.soft.api.entity.User;
import task1.soft.api.exception.NoDeletePermissionException;
import task1.soft.api.repo.DepartmentRepository;
import task1.soft.api.exception.NotFoundException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private DepartmentRepository departmentRepository;
    private ModelMapper modelMapper;

    public Department createDepartment(DepartmentDTO departmentDTO) {
        Department department = new Department();
        department.setName(departmentDTO.getName());
        department.setCity(departmentDTO.getCity());
        department.setMinSalary(departmentDTO.getMinSalary());
        department.setMaxSalary(departmentDTO.getMaxSalary());
        departmentRepository.save(department);
        return department;
    }

    @Override
    public Department updateDepartment(Department department) {


        department.setId(department.getId());
        department.setName(department.getName());
        department.setCity(department.getCity());
        department.setMinSalary(department.getMinSalary());
        department.setMaxSalary(department.getMaxSalary());
        departmentRepository.save(department);
        return department;
    }

    @Override
    public Department findOne(Long idDepart) throws NotFoundException {

        Optional<Department> department = Optional.ofNullable(departmentRepository.findOne(idDepart));
        return department.orElseThrow(() -> new NotFoundException("There is no Department with id: " + idDepart));
    }

    @Override
    public void delete(Long idDepart) throws NoDeletePermissionException {
        Department department = findOne(idDepart);
        if (department.getEmployees().isEmpty()) {
            departmentRepository.delete(idDepart);
        } else {
            throw new NoDeletePermissionException("Cannot delete this department because there are still employees");
        }

    }

    @Override
    public DepartmentDTO getAllDepartmentDetails(Long idDepart) {
        Department department = this.findOne(idDepart);
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(idDepart);
        departmentDTO.setName(department.getName());
        departmentDTO.setCity(department.getCity());
        departmentDTO.setMaxSalary(department.getMaxSalary());
        departmentDTO.setMinSalary(department.getMinSalary());
        departmentDTO.setNumberOfEmployees(departmentRepository.countEmployeesByDepartId(idDepart));

        departmentDTO.setAverageSalary(departmentRepository.countAverageSalaries(idDepart));
        if (departmentDTO.getNumberOfEmployees() == 0) {
            departmentDTO.setAverageSalary(BigDecimal.valueOf(0));
        }
        BigDecimal medianSalary = calculateMedian(idDepart);
        departmentDTO.setMedianSalary(medianSalary);
        User head = departmentRepository.findHeadByIdDepart(idDepart);
        if (head != null) {
            EmployeeReadDTO headDTO = modelMapper.map(head, EmployeeReadDTO.class);
            departmentDTO.setHeadOfDepartment(headDTO);
        }
        return departmentDTO;
    }

    @Override
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    @Override
    public BigDecimal calculateMedian(Long idDepart) {
        return this.calcMedian(idDepart);
    }


    @Override
    public User findHeadByIdDepart(Long idDepart) {
        return departmentRepository.findHeadByIdDepart(idDepart);
    }


    /**
     * Add new employee to the department. The method keeps
     * relationships consistency:
     * * this department is set as the employee owner
     */
    @Override
    public void addEmployee(Long idDepart, User employee) {

        Department department = this.findOne(idDepart);
        List<User> employees = department.getEmployees();
        //prevent endless loop
        if (employees.contains(employee)) {
            return;
        }
        //add new employee
        employees.add(employee);
        //set myself into the employee account
        employee.setDepartment(department);

    }

    /**
     * Removes the employee from the department. The method keeps
     * relationships consistency:
     */
    @Override
    public void removeEmployee(Long idDepart, User employee) {
        Department department = this.findOne(idDepart);
        List<User> employees = department.getEmployees();
        //prevent endless loop
        if (!employees.contains(employee)) {
            return;
        }
        //remove the employee
        employees.remove(employee);
        //remove myself from the employee
        employee.setDepartment(null);
    }

    private BigDecimal calcMedian(Long idDepart) {

        Department department = this.findOne(idDepart);
        List<BigDecimal> salariesList = department.getEmployees().stream().map(User::getSalary).sorted().collect(Collectors.toList());
        // Calculate median (middle number)
        BigDecimal median;
        double pos1 = Math.floor((salariesList.size() - 1.0) / 2.0);
        double pos2 = Math.ceil((salariesList.size() - 1.0) / 2.0);
        if (salariesList.size() == 0) {
            return BigDecimal.valueOf(0);
        }
        if (pos1 == pos2) {
            median = salariesList.get((int) pos1);
        } else {
            median = (salariesList.get((int) pos1)).add(salariesList.get((int) pos2)).divide(BigDecimal.valueOf(2));
        }

        return median;
    }

}

