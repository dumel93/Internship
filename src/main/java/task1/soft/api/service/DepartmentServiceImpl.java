package task1.soft.api.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import task1.soft.api.dto.DepartmentDTO;
import task1.soft.api.dto.EmployeeReadDTO;
import task1.soft.api.entity.Department;
import task1.soft.api.entity.User;
import task1.soft.api.repo.DepartmentRepository;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private DepartmentRepository departmentRepository;
    private ModelMapper modelMapper;

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
        department.setMinSalary(department.getMinSalary());
        department.setMaxSalary(department.getMaxSalary());
        departmentRepository.save(department);
        return department;
    }

    @Override
    public Department findOne(Long id) {
        return departmentRepository.findOne(id);
    }

    @Override
    public void delete(Long idDepart) {
        Department department = departmentRepository.findOne(idDepart);
        if (department.getEmployees().isEmpty()) {
            departmentRepository.delete(idDepart);
        }
    }

    @Override
    public DepartmentDTO getAllDepartmentDetails(Long idDepart) {
        Department department = departmentRepository.findOne(idDepart);
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(idDepart);
        departmentDTO.setName(department.getName());
        departmentDTO.setCity(department.getCity());
        departmentDTO.setMaxSalary(department.getMaxSalary());
        departmentDTO.setMinSalary(department.getMinSalary());
        departmentDTO.setNumberOfEmployees(departmentRepository.countEmployeesByDepartId(idDepart));

        departmentDTO.setAverageSalary(departmentRepository.countAverageSalaries(idDepart));
        if (departmentDTO.getNumberOfEmployees() == 0) {
            departmentDTO.setAverageSalary(new BigDecimal(("0")));
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

