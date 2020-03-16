package task1.soft.api.restcontroller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
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
import task1.soft.api.exception.NotFoundException;
import task1.soft.api.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@Slf4j
@Secured("ROLE_CEO")
@AllArgsConstructor
@RequestMapping(value = "/employees", produces = "application/json")
public class EmployeeRestController {

    private UserService userService;
    private ModelMapper modelMapper;

    // -------------------Get an Employee/s-------------------------------------------
    @Secured("ROLE_HEAD")
    @GetMapping
    public List<EmployeeReadDTO> getAllEmployees(@AuthenticationPrincipal UserDetails auth) {

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        List<User> employees = userService.findAllEmployees();

        return employees.stream()
                .map(employee -> modelMapper.map(employee, EmployeeReadDTO.class))
                .map(userService::getDataFromEmployeeReadDTO)
                .collect(Collectors.toList());
    }

    @Secured("ROLE_HEAD")
    @GetMapping("/{id}")
    public EmployeeReadDTO getEmployeeById(@PathVariable Long id, @AuthenticationPrincipal UserDetails auth) throws NotFoundException {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User user = userService.findEmployee(id);
        EmployeeReadDTO employeeReadDTO = modelMapper.map(user, EmployeeReadDTO.class);
        return userService.getDataFromEmployeeReadDTO(employeeReadDTO);
    }


    @Secured({"ROLE_HEAD", "ROLE_EMPLOYEE"})
    @GetMapping("/me")
    public EmployeeReadDTO getEmployee(@AuthenticationPrincipal UserDetails auth) throws NotFoundException {
        User currentUser = userService.findByEmail(auth.getUsername());
        userService.setLoginTime(currentUser.getId());
        EmployeeReadDTO employeeReadDTO = modelMapper.map(currentUser, EmployeeReadDTO.class);
        return userService.getDataFromEmployeeReadDTO(employeeReadDTO);
    }

    @PreAuthorize(value = "!hasRole('CEO')")
    @Secured({"ROLE_HEAD", "ROLE_EMPLOYEE"})
    @GetMapping("/departments")
    public List<EmployeeReadDTO> findAllEmployeesOfDepartment(@AuthenticationPrincipal UserDetails auth) {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User currentUser = userService.findByEmail(auth.getUsername());
        Department department = currentUser.getDepartment();
        List<User> employees = userService.findAllEmployeesOfDepartment(department.getId());
        return employees.stream()
                .map(entity -> modelMapper.map(entity, EmployeeReadDTO.class))
                .map(userService::getDataFromEmployeeReadDTO)
                .collect(Collectors.toList());

    }

    @Secured("ROLE_HEAD")
    @GetMapping("/departments/{id}")
    public List<EmployeeReadDTO> findEmployeesOfDepartment(@PathVariable Long id, @AuthenticationPrincipal UserDetails auth) {

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        List<User> employees = userService.findAllEmployeesOfDepartment(id);
        return employees.stream()
                .map(entity -> modelMapper.map(entity, EmployeeReadDTO.class))
                .map(userService::getDataFromEmployeeReadDTO)
                .collect(Collectors.toList());
    }

    // -------------------Create an Employee-------------------------------------------
    @Secured("ROLE_HEAD")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public EmployeeReadDTO createEmployee(@AuthenticationPrincipal UserDetails auth,
                                          @Valid @RequestBody EmployeeDTO employeeDTO,
                                          SecurityContextHolderAwareRequestWrapper request) throws NotFoundException {

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User currentUser = userService.findByEmail(auth.getUsername());
        if (request.isUserInRole("ROLE_CEO")) {
            User user = userService.createEmployee(employeeDTO);
            employeeDTO.setId(user.getId());
        }
        if (request.isUserInRole("ROLE_HEAD")) {
            employeeDTO.setDepartmentId(currentUser.getDepartment().getId());
            employeeDTO.setHead(false);
            User user = userService.createEmployee(employeeDTO);
            employeeDTO.setId(user.getId());
        }

        return modelMapper.map(employeeDTO, EmployeeReadDTO.class);


    }


    // -------------------Update an Employee-------------------------------------------

    @Secured({"ROLE_HEAD", "ROLE_EMPLOYEE"})
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public EmployeeReadDTO updateEmployee(@AuthenticationPrincipal UserDetails auth,
                                          @Valid @RequestBody EmployeeDTO employeeDTO,
                                          @PathVariable Long id,
                                          SecurityContextHolderAwareRequestWrapper request) {

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User employee = userService.findEmployee(id);
        User currentUser = userService.findByEmail(auth.getUsername());
        employeeDTO.setId(id);
        if (request.isUserInRole("ROLE_CEO") && id != 1) {
            userService.updateEmployee(employeeDTO);
        }
        if (request.isUserInRole("ROLE_HEAD") && id != 1) {
            if (userService.isUserInHeadDepart(employee, currentUser.getDepartment())) {
                userService.updateEmployee(employeeDTO);
            }
        }
        return modelMapper.map(employeeDTO, EmployeeReadDTO.class);

    }

    @Secured({"ROLE_HEAD", "ROLE_EMPLOYEE"})
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/me")
    public EmployeeReadDTO updateAuthEmployee(@AuthenticationPrincipal UserDetails auth,
                                              @Valid @RequestBody EmployeeDTO employeeDTO) {

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User currentUser = userService.findByEmail(auth.getUsername());
        employeeDTO.setId(currentUser.getId());
        User userAfterUpdate = userService.updateEmployee(employeeDTO);
        return modelMapper.map(userAfterUpdate, EmployeeReadDTO.class);

    }


    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/departments/head/{id}")
    public EmployeeReadDTO setHead(@PathVariable Long id, @AuthenticationPrincipal UserDetails auth) {

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User employee = userService.findEmployee(id);
        if (id != 1) {

            EmployeeDTO employeeDTO = modelMapper.map(employee, EmployeeDTO.class);
            employeeDTO.setHead(true);
            userService.setHead(employeeDTO, employee, employee.getDepartment());
            User userUpdated = userService.updateEmployee(employeeDTO);
            return modelMapper.map(userUpdated, EmployeeReadDTO.class);
        }
        return modelMapper.map(employee, EmployeeReadDTO.class);

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

        if (request.isUserInRole("ROLE_CEO") && id != 1) {
            userService.updateEmployee(employeeDTO);
        }
        if (request.isUserInRole("ROLE_HEAD") && id != 1) {
            if (userService.isUserInHeadDepart(employee, currentUser.getDepartment())) {
                userService.updateEmployee(employeeDTO);
            }
        }

    }

    @Secured("ROLE_HEAD")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/salary")
    public EmployeeReadDTO setSalary(@AuthenticationPrincipal UserDetails auth,
                                     @Valid @RequestBody EmployeeSalaryDTO employeeSalaryDTO,
                                     @PathVariable Long id,
                                     SecurityContextHolderAwareRequestWrapper request) {

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User employee = userService.findEmployee(id);
        employee.setSalary(employeeSalaryDTO.getSalary());
        userService.setSalary(employee);
        EmployeeDTO employeeDTO = modelMapper.map(employee, EmployeeDTO.class);
        User currentUser = userService.findByEmail(auth.getUsername());

        if (request.isUserInRole("ROLE_CEO") && id != 1) {
            userService.updateEmployee(employeeDTO);
        }
        if (request.isUserInRole("ROLE_HEAD") && id != 1) {
            if (userService.isUserInHeadDepart(employee, currentUser.getDepartment())) {
                userService.updateEmployee(employeeDTO);
            }
        }
        return modelMapper.map(employee, EmployeeReadDTO.class);


    }

    @Secured("ROLE_HEAD")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/active")
    public EmployeeReadDTO disable(@AuthenticationPrincipal UserDetails auth,
                                   @PathVariable Long id,
                                   SecurityContextHolderAwareRequestWrapper request) {

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User employee = userService.findEmployee(id);
        if (employee.isActive()) {
            employee.setActive(false);
        } else {
            employee.setActive(true);
        }
        userService.setActivity(employee);
        EmployeeDTO employeeDTO = modelMapper.map(employee, EmployeeDTO.class);
        User currentUser = userService.findByEmail(auth.getUsername());
        if (request.isUserInRole("ROLE_CEO") && id != 1) {
            User userUpdated = userService.updateEmployee(employeeDTO);
            return modelMapper.map(userUpdated, EmployeeReadDTO.class);
        }
        if (request.isUserInRole("ROLE_HEAD") && id != 1) {
            if (userService.isUserInHeadDepart(employee, currentUser.getDepartment())) {
                User userUpdated = userService.updateEmployee(employeeDTO);
                return modelMapper.map(userUpdated, EmployeeReadDTO.class);
            }
        }
        return modelMapper.map(employee, EmployeeReadDTO.class);

    }

    // -------------------Delete an Employee-------------------------------------------
    @Secured("ROLE_HEAD")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id,
                               @AuthenticationPrincipal UserDetails auth,
                               SecurityContextHolderAwareRequestWrapper request) throws NotFoundException {

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User employee = userService.findEmployee(id);
        User currentUser = userService.findByEmail(auth.getUsername());
        if (request.isUserInRole("ROLE_CEO") && id != 1) {
            userService.deleteEmployee(employee);
        }
        if (request.isUserInRole("ROLE_HEAD") && id != 1) {
            if (userService.isUserInHeadDepart(employee, currentUser.getDepartment())) {
                userService.deleteEmployee(employee);
            }
        }

    }

}
