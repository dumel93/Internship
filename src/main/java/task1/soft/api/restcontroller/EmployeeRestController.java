package task1.soft.api.restcontroller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
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
@RequiredArgsConstructor
@RequestMapping(value = "/employees")
public class EmployeeRestController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    // -------------------Get an Employee/s-------------------------------------------
    @PreAuthorize("hasRole('ROLE_HEAD') or hasRole('ROLE_CEO')")
    @GetMapping
    public List<EmployeeReadDTO> getAllEmployees(@AuthenticationPrincipal UserDetails auth) {

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        List<User> employees = userService.findAllEmployees();

        return employees.stream()
                .map(employee -> modelMapper.map(employee, EmployeeReadDTO.class))
                .map(userService::getDataFromEmployeeReadDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ROLE_HEAD') or hasRole('ROLE_CEO') or hasRole('ROLE_EMPLOYEE')")
    @GetMapping("/{id}")
    public EmployeeReadDTO getEmployeeById(@PathVariable Long id, @AuthenticationPrincipal UserDetails auth) throws NotFoundException {
        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User user = userService.findEmployee(id);
        EmployeeReadDTO employeeReadDTO = modelMapper.map(user, EmployeeReadDTO.class);
        return userService.getDataFromEmployeeReadDTO(employeeReadDTO);
    }


    @PreAuthorize("(hasRole('ROLE_HEAD') or hasRole('ROLE_EMPLOYEE')) and !hasRole('ROLE_CEO')")
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

    @PreAuthorize("hasRole('ROLE_HEAD') or hasRole('ROLE_CEO')")
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
    @PreAuthorize("hasRole('ROLE_HEAD') or hasRole('ROLE_CEO')")
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

    @PreAuthorize("hasRole('ROLE_HEAD') or hasRole('ROLE_CEO') or hasRole('ROLE_EMPLOYEE')")
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
        if (request.isUserInRole("ROLE_CEO") ||
                (request.isUserInRole("ROLE_HEAD") && userService.isUserInHeadDepart(employee, currentUser.getDepartment())) ||
                (request.isUserInRole("ROLE_EMPLOYEE") && id.equals(currentUser.getId()))) {
            userService.updateEmployee(employeeDTO);
        }

        return modelMapper.map(employeeDTO, EmployeeReadDTO.class);

    }


    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/departments/head/{id}")
    public EmployeeReadDTO setHead(@PathVariable Long id, @AuthenticationPrincipal UserDetails auth) {

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User employee = userService.findEmployee(id);
        EmployeeDTO employeeDTO = modelMapper.map(employee, EmployeeDTO.class);
        employeeDTO.setHead(true);
        userService.setHead(employeeDTO, employee, employee.getDepartment());
        User userUpdated = userService.updateEmployee(employeeDTO);
        return modelMapper.map(userUpdated, EmployeeReadDTO.class);


    }

    @PreAuthorize("hasRole('ROLE_HEAD') or hasRole('ROLE_CEO')")
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

        if (request.isUserInRole("ROLE_CEO") || (request.isUserInRole("ROLE_HEAD") && userService.isUserInHeadDepart(employee, currentUser.getDepartment()))) {
            userService.updateEmployee(employeeDTO);
        }

    }

    @PreAuthorize("hasRole('ROLE_HEAD') or hasRole('ROLE_CEO')")
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

        if (request.isUserInRole("ROLE_CEO") || (request.isUserInRole("ROLE_HEAD") && userService.isUserInHeadDepart(employee, currentUser.getDepartment()))) {
            userService.updateEmployee(employeeDTO);
        }
        return modelMapper.map(employee, EmployeeReadDTO.class);

    }

    @PreAuthorize("hasRole('ROLE_HEAD') or hasRole('ROLE_CEO')")
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
        if (request.isUserInRole("ROLE_CEO") || (request.isUserInRole("ROLE_HEAD") && userService.isUserInHeadDepart(employee, currentUser.getDepartment()))) {
            userService.updateEmployee(employeeDTO);
        }
        return modelMapper.map(employee, EmployeeReadDTO.class);

    }

    // -------------------Delete an Employee-------------------------------------------
    @PreAuthorize("hasRole('ROLE_HEAD') or hasRole('ROLE_CEO')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id,
                               @AuthenticationPrincipal UserDetails auth,
                               SecurityContextHolderAwareRequestWrapper request) throws NotFoundException {

        userService.setLoginTime(userService.findByEmail(auth.getUsername()).getId());
        User employee = userService.findEmployee(id);
        User currentUser = userService.findByEmail(auth.getUsername());
        if (request.isUserInRole("ROLE_CEO") || (request.isUserInRole("ROLE_HEAD") && userService.isUserInHeadDepart(employee, currentUser.getDepartment()))) {
            userService.deleteEmployee(employee);
        }

    }

}
