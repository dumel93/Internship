package task1.soft.api.service;


import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import task1.soft.api.dto.DepartmentDTO;
import task1.soft.api.dto.EmployeeReadDTO;
import task1.soft.api.entity.Department;
import task1.soft.api.entity.User;
import task1.soft.api.exception.NoDeletePermissionException;
import task1.soft.api.exception.NotFoundException;
import task1.soft.api.repo.DepartmentRepository;

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
        Department department = modelMapper.map(departmentDTO, Department.class);
        departmentRepository.save(department);
        return department;
    }

    @Override
    public Department updateDepartment(DepartmentDTO departmentDTO) {
        Department department = modelMapper.map(departmentDTO, Department.class);
        department.setId(departmentDTO.getId());
        departmentRepository.save(department);
        return department;
    }

    @Override
    public Department findDepartment(Long idDepart) throws NotFoundException {

        Optional<Department> department = Optional.ofNullable(departmentRepository.findOne(idDepart));
        return department.orElseThrow(() -> new NotFoundException("There is no Department with id: " + idDepart));
    }

    @Override
    public void deleteDepartment(Long idDepart) throws NoDeletePermissionException, NotFoundException {
        Department department = findDepartment(idDepart);
        if (department.getEmployees().isEmpty()) {
            departmentRepository.delete(idDepart);
        } else {
            throw new NoDeletePermissionException("Cannot delete this department because there are still employees");
        }

    }

    @Override
    public DepartmentDTO getAllDepartmentDetails(Long idDepart) {
        Department department = this.findDepartment(idDepart);
        DepartmentDTO departmentDTO = modelMapper.map(department, DepartmentDTO.class);
        departmentDTO.setNumberOfEmployees(departmentRepository.countEmployeesByIdDepart(idDepart));
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
    public List<Department> findAllDepartments() {
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

    private BigDecimal calcMedian(Long idDepart) {

        Department department = this.findDepartment(idDepart);
        List<BigDecimal> salariesList = department.getEmployees().stream().map(User::getSalary).sorted().collect(Collectors.toList());
        // Calculate median (middle number)
        BigDecimal median;
        double pos1 = Math.floor((salariesList.size() - 1.0) / 2.0);
        double pos2 = Math.ceil((salariesList.size() - 1.0) / 2.0);
        if (salariesList.isEmpty()) {
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

