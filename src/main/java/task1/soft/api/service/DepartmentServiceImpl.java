package task1.soft.api.service;


import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import task1.soft.api.dto.DepartmentDTO;
import task1.soft.api.dto.EmployeeReadDTO;
import task1.soft.api.entity.Department;
import task1.soft.api.entity.User;
import task1.soft.api.repo.DepartmentRepository;
import task1.soft.api.repo.UserRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DepartmentServiceImpl implements DepartmentService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    public Department createDepartment(String name, String city) {
        Department department = new Department();
        department.setName(name);
        department.setCity(city);
        departmentRepository.save(department);
        return department;
    }


    @Override
    public Department updateDepartment(Department department) {

        department.setId(department.getId());
        department.setName(department.getName());
        department.setCity(department.getCity());
        departmentRepository.save(department);
        return department;
    }

    @Override
    public Department findOne(Long id) {
        return departmentRepository.findOne(id);
    }

    @Override
    public void delete(Department department) {
        departmentRepository.delete(department);
    }

    @Override
    public DepartmentDTO setEmployeesDetails(Long idDepart) {

        Department department = departmentRepository.findOne(idDepart);
        DepartmentDTO departmentDTO = modelMapper.map(department, DepartmentDTO.class);
        departmentDTO.setNumberOfEmployees(departmentRepository.countEmployeesByDepartId(idDepart));

        departmentDTO.setAverageSalary(departmentRepository.countAverageSalaries(idDepart));
        if (departmentDTO.getNumberOfEmployees() == 0) {
            departmentDTO.setAverageSalary(new BigDecimal(("0")));
        }
        departmentDTO.setMedianSalary(this.calcMedian(idDepart));
        User head = departmentRepository.findHeadByIdDepart(idDepart);
        if (head != null) {
            departmentDTO.setHeadOfDepartment(modelMapper.map(head, EmployeeReadDTO.class));
        }
        return departmentDTO;

    }

    @Override
    public List<Department> findAll(Integer offset, Integer limit, String sortBy, String orderBy) {
        if (orderBy.toUpperCase().equals("DESC")) {
            Sort sortdesc = new Sort(new Sort.Order(Sort.Direction.DESC, sortBy));
            Pageable pageable = new PageRequest(offset, limit, sortdesc);
            return departmentRepository.findAll(pageable).getContent();
        }
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, sortBy));
        Pageable pageable = new PageRequest(offset, limit, sort);
        return departmentRepository.findAll(pageable).getContent();
    }

    private BigDecimal calcMedian(Long idDepart) {

        Department department = departmentRepository.findOne(idDepart);
        List<BigDecimal> salariesList = department.getEmployees().stream().map(User::getSalary).sorted().collect(Collectors.toList());
        // Calculate median (middle number)
        BigDecimal median;
        double pos1 = Math.floor((salariesList.size() - 1.0) / 2.0);
        double pos2 = Math.ceil((salariesList.size() - 1.0) / 2.0);
        if (salariesList.size() == 0) {
            return new BigDecimal("0");
        }
        if (pos1 == pos2) {
            median = salariesList.get((int) pos1);
        } else {
            median = (salariesList.get((int) pos1)).add(salariesList.get((int) pos2)).divide(new BigDecimal("2"));
        }

        return median;
    }
}

