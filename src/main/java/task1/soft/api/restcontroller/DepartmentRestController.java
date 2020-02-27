package task1.soft.api.restcontroller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import task1.soft.api.repo.DepartmentRepository;
import task1.soft.api.repo.UserRepository;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Min;



@RestController
public class DepartmentRestController {

    private final DepartmentRepository departmentRepository;

    private final UserRepository userRepository;

    @Autowired
    public DepartmentRestController(DepartmentRepository departmentRepository, UserRepository userRepository) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @ResponseBody
    @GetMapping("/departments")
    public List<department> hello() {
        return departmentRepository.findAll();
    }

    @PostMapping("/departments")
    @ResponseStatus(HttpStatus.CREATED)
    public department add(@RequestBody department department) {

        departmentRepository.save(department);

        return department;


    }

    @PutMapping("/departments/{id}")
    department saveOrUpdate(@Valid @RequestBody department department, @PathVariable Long id) {

        department.setId(id);
        departmentRepository.save(department);
        return department;

    }


    @PutMapping("/departments/{id}/salaries")
    department setMinSalaryOrMaxSalary(@RequestBody department department, @PathVariable Long id) {

        department departmenttoSave = departmentRepository.findOne(id);
        float max_salary = department.getMaxSalary();
        float min_salary = department.getMinSalary();
        departmenttoSave.setMaxSalary(max_salary);
        departmenttoSave.setMinSalary(min_salary);
        departmenttoSave.setId(id);
        departmentRepository.save(departmenttoSave);
        return departmenttoSave;

    }

    @DeleteMapping("/departments/{id}")
    void delOne(@PathVariable @Min(value = 1, message = "must be greater than or equal to 1") Long id) {
        department department = departmentRepository.findOne(id);
        if (department.getNumberOfEmployyes() == 0) {
            departmentRepository.delete(department);
        }


    }

}
