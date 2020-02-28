package task1.soft.api.restcontroller;


import ch.qos.logback.core.db.dialect.DBUtil;
import org.apache.el.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import task1.soft.api.dto.DepartmentDTO;
import task1.soft.api.dto.DepartmentSalariesDTO;
import task1.soft.api.entity.Department;
import task1.soft.api.repo.DepartmentRepository;
import task1.soft.api.repo.UserRepository;
import task1.soft.api.service.DepartmentService;
import task1.soft.api.util.DtoUtils;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Secured("ROLE_CEO")
@RestController
@RequestMapping(produces = "application/json",value = "/departments")
public class DepartmentRestController {

    private final DepartmentRepository departmentRepository;
    private final DepartmentService departmentService;
    private final UserRepository userRepository;
    private  final ModelMapper modelMapper;

    @Autowired
    public DepartmentRestController(DepartmentRepository departmentRepository, DepartmentService departmentService, UserRepository userRepository, ModelMapper modelMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentService = departmentService;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }



    @GetMapping(value = "/")
    public List<Department> getDepartments() {
        return departmentRepository.findAll();
    }


    @GetMapping(value = "/{id}")
    public Department getDepartment(@PathVariable("id") Long id) {
        return departmentRepository.findOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentDTO createDepartment(@Valid @RequestBody DepartmentDTO departmentDTO) throws ParseException {

        return (DepartmentDTO) departmentService.createDepartment(departmentDTO.getName(),departmentDTO.getCity());

    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    Department UpdateDepartment(@Valid @RequestBody DepartmentDTO departmentDTO, @PathVariable Long id) throws ParseException {
        Department department=modelMapper.map(departmentDTO, Department.class);

        departmentService.updateDepartment(department);
        return department;
    }

    @PutMapping("/salaries/{id}")
    Department setMinSalaryOrMaxSalary(@RequestBody DepartmentSalariesDTO departmentSalariesDTO, @PathVariable Long id) throws ParseException {

        Department department=modelMapper.map(departmentSalariesDTO, Department.class);
        department.setId(id);;
        department.setMinSalary(departmentSalariesDTO.getMinSalary());
        department.setMaxSalary(departmentSalariesDTO.getMaxSalary());
        departmentService.save(department);
        return department;

    }

    @DeleteMapping("/{id}")
    void deleteDepartment(@PathVariable @Min(value = 1, message = "must be greater than or equal to 1") Long id) {
        Department department = departmentRepository.findOne(id);
        if (department.getNumberOfEmployees() == 0) {
            departmentRepository.delete(department);
        }

    }

}
