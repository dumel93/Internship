package task1.soft.api.restcontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import task1.soft.api.dto.EmployeeDTO;
import task1.soft.api.dto.EmployeePasswordDTO;
import task1.soft.api.dto.EmployeeReadDTO;
import task1.soft.api.dto.EmployeeSalaryDTO;
import task1.soft.api.entity.Department;
import task1.soft.api.entity.User;
import task1.soft.api.service.DepartmentService;
import task1.soft.api.service.UserService;
import task1.soft.api.exception.NotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/employees", produces = "application/json")
public class EmployeeRestController {


    private final UserService userService;
    private final DepartmentService departmentService;
    private final ModelMapper modelMapper;


    // -------------------Get an Employee/s-------------------------------------------
    @Secured({"ROLE_CEO", "ROLE_HEAD"})
    @GetMapping
    public List<EmployeeReadDTO> getAllEmployees(@AuthenticationPrincipal UserDetails auth) {

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        List<User> employees = userService.findAllEmployees();

        return employees.stream()
                .map(employee -> modelMapper.map(employee, EmployeeReadDTO.class))
                .collect(Collectors.toList());
    }

    @Secured({"ROLE_CEO", "ROLE_HEAD"})
    @GetMapping("/{id}")
    public EmployeeReadDTO getEmployeeById(@PathVariable Long id, @AuthenticationPrincipal UserDetails auth) throws NotFoundException {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User user = userService.findEmployee(id);
        return modelMapper.map(user, EmployeeReadDTO.class);
    }


    @Secured({"ROLE_HEAD", "ROLE_EMPLOYEE"})
    //ceo no permition
    @GetMapping("/departments")
    public List<EmployeeReadDTO> findAllEmployeesOfDepartment(@AuthenticationPrincipal UserDetails auth) {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User currentUser = userService.findByEmail(auth.getUsername());
        Department department = currentUser.getDepartment();
        List<User> employees = userService.findAllEmployeesOfDepartment(department.getId());
        return employees.stream()
                .map(entity -> modelMapper.map(entity, EmployeeReadDTO.class))
                .collect(Collectors.toList());


    }

    @Secured({"ROLE_CEO", "ROLE_HEAD"})
    @GetMapping("/departments/{id}")
    public List<EmployeeReadDTO> findEmployeesOfDepartment(@PathVariable Long id, @AuthenticationPrincipal UserDetails auth) {

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        List<User> employees = userService.findAllEmployeesOfDepartment(id);
        return employees.stream()
                .map(entity -> modelMapper.map(entity, EmployeeReadDTO.class))
                .collect(Collectors.toList());
    }

    // -------------------Create an Employee-------------------------------------------
    @Secured({"ROLE_CEO", "ROLE_HEAD"})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public EmployeeReadDTO createEmployee(@AuthenticationPrincipal UserDetails auth, @Valid @RequestBody EmployeeDTO employeeDTO) throws NotFoundException{

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        userService.createEmployee(employeeDTO);
        return modelMapper.map(employeeDTO, EmployeeReadDTO.class);
    }


    // -------------------Update an Employee-------------------------------------------

    @Secured("ROLE_HEAD")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    void updateEmployee(@AuthenticationPrincipal UserDetails auth, @Valid @RequestBody EmployeeDTO employeeDTO, @PathVariable Long id) {

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User employee = userService.findEmployee(id);
        employeeDTO.setId(employee.getId());
        userService.updateEmployee(employeeDTO);
    }

    @Secured("ROLE_CEO")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/departments/head/{id}")
    public void setHead(@PathVariable Long id, @AuthenticationPrincipal UserDetails auth) {

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User employee = userService.findEmployee(id);
        User head = departmentService.findHeadByIdDepart(employee.getDepartment().getId());
        employee.setHead(true);
        EmployeeDTO employeeDTO = modelMapper.map(employee, EmployeeDTO.class);

        if (head == null) {
            userService.updateEmployee(employeeDTO);
        } else {
            EmployeeDTO headDTO = modelMapper.map(head, EmployeeDTO.class);
            head.setHead(false);
            userService.updateEmployee(employeeDTO);
            userService.updateEmployee(headDTO);

        }
    }

    @Secured("ROLE_HEAD")
    @PutMapping("/{id}/password")
    @ResponseStatus(HttpStatus.OK)
    public void updatePassword(@AuthenticationPrincipal UserDetails auth,
                               @Valid @RequestBody EmployeePasswordDTO employeePasswordDTO,
                               @PathVariable Long id,
                               SecurityContextHolderAwareRequestWrapper request) {

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User employee = userService.findEmployee(id);
        employee.setPassword(employeePasswordDTO.getPassword());
        EmployeeDTO employeeDTO = modelMapper.map(employee, EmployeeDTO.class);
        User currentUser = userService.findByEmail(auth.getUsername());

        if (request.isUserInRole("ROLE_CEO")) {
            userService.updateEmployee(employeeDTO);
        }
        if (request.isUserInRole("ROLE_HEAD")) {
            if (userService.isUserInHeadDepart(employee, currentUser.getDepartment())) {
                userService.updateEmployee(employeeDTO);
            }
        }

    }

    @Secured("ROLE_HEAD")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/salary")
    public void setSalary(@AuthenticationPrincipal UserDetails auth,
                          @Valid @RequestBody EmployeeSalaryDTO employeeSalaryDTO,
                          @PathVariable Long id,
                          SecurityContextHolderAwareRequestWrapper request) {

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User employee = userService.findEmployee(id);
        employee.setSalary(employeeSalaryDTO.getSalary());
        EmployeeDTO employeeDTO = modelMapper.map(employee, EmployeeDTO.class);
        userService.setSalary(employee);
        User currentUser = userService.findByEmail(auth.getUsername());

        if (request.isUserInRole("ROLE_CEO")) {
            userService.updateEmployee(employeeDTO);
        }
        if (request.isUserInRole("ROLE_HEAD")) {
            if (userService.isUserInHeadDepart(employee, currentUser.getDepartment())) {
                userService.updateEmployee(employeeDTO);
            }
        }

    }

    @Secured("ROLE_HEAD")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/disable")
    public void disable(@AuthenticationPrincipal UserDetails auth,
                        @PathVariable Long id,
                        SecurityContextHolderAwareRequestWrapper request) {

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User employee = userService.findEmployee(id);
        employee.setActive(false);
        EmployeeDTO employeeDTO= modelMapper.map(employee,EmployeeDTO.class);
        User currentUser = userService.findByEmail(auth.getUsername());
        if (request.isUserInRole("ROLE_CEO")) {
            userService.updateEmployee(employeeDTO);
        }
        if (request.isUserInRole("ROLE_HEAD")) {
            if (userService.isUserInHeadDepart(employee, currentUser.getDepartment())) {
                userService.updateEmployee(employeeDTO);
            }
        }

    }

    // -------------------Delete an Employee-------------------------------------------
    @Secured("ROLE_HEAD")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable @Min(value = 1, message = "must be greater than or equal to 1") Long id,
                                    @AuthenticationPrincipal UserDetails auth,
                                    SecurityContextHolderAwareRequestWrapper request) throws NotFoundException {

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User employee = userService.findEmployee(id);
        User currentUser = userService.findByEmail(auth.getUsername());
        if (request.isUserInRole("ROLE_CEO")) {
            userService.deleteEmployee(employee);
        }
        if (request.isUserInRole("ROLE_HEAD")) {
            if (userService.isUserInHeadDepart(employee, currentUser.getDepartment())) {
                userService.deleteEmployee(employee);
            }
        }

    }

}
