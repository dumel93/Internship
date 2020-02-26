package task1.soft.api.restcontroller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import task1.soft.api.entity.Departament;
import task1.soft.api.entity.User;
import task1.soft.api.repo.DepartmentRepository;
import task1.soft.api.repo.UserRepository;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

////
//@Secured("ROLE_CEO")
@RestController
public class DepartmentRestController {

    private final DepartmentRepository departmentRepository;

    private  final  UserRepository userRepository;
    @Autowired
    public DepartmentRestController(DepartmentRepository departmentRepository, UserRepository userRepository) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }
    @Transactional
    @ResponseBody
    @GetMapping("/departments")
    public List<Departament> hello(){
       return departmentRepository.findAll();

    }



    @PostMapping("/departments")
    @ResponseStatus(HttpStatus.CREATED)
    public Departament add(@RequestBody Departament departament) {

        departmentRepository.save(departament);

        return departament;



    }

    @PutMapping("/departments/{id}")
    Departament saveOrUpdate(@Valid @RequestBody Departament departament, @PathVariable Long id) {

        departament.setId(id);
        departmentRepository.save(departament);
        return departament;

    }


    @PutMapping("/departments/{id}/salaries")
    Departament setMinSalaryOrMaxSalary(@RequestBody Departament departament, @PathVariable Long id) {

        Departament departamenttoSave=departmentRepository.findOne(id);
        float max_salary=departament.getMaxSalary();
        float min_salary=departament.getMinSalary();
        departamenttoSave.setMaxSalary(max_salary);
        departamenttoSave.setMinSalary(min_salary);
        departamenttoSave.setId(id);
        departmentRepository.save(departamenttoSave);
        return departamenttoSave;

    }

    @DeleteMapping("/departments/{id}")

    void delOne(@PathVariable @Min(value = 1,message = "must be greater than or equal to 1") Long id) {
        Departament departament=departmentRepository.findOne(id);
        if(departament.getNumberOfEmployyes()==0){
            departmentRepository.delete(departament);
        }






    }

}
