package task1.soft.api.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import task1.soft.api.dto.EmployeeDTO;
import task1.soft.api.entity.*;
import task1.soft.api.exception.NotFoundException;
import task1.soft.api.exception.UserExistsException;
import task1.soft.api.repo.PhoneRepository;
import task1.soft.api.repo.RoleRepository;
import task1.soft.api.repo.UserRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;


@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final PhoneRepository phoneRepository;

    private final DepartmentService departmentService;


    @Override
    public void setupCEO() {
        User ceo = new User();
        ceo.setFirstName("admin");
        ceo.setLastName("admin");
        ceo.setEmail("ceo@pgs.com");
        ceo.setSalary(new BigDecimal("20000"));
        ceo.setActive(true);
        ceo.setPassword(passwordEncoder.encode("admin123"));
        Role userRole = roleRepository.findByName("ROLE_CEO");
        ceo.setRoles(new HashSet<>(Collections.singletonList(userRole)));

        Set<Phone> phones = new HashSet<>();
        Phone phone = new Phone();
        phone.setNumber("533-202-020");
        phone.setType(PhoneType.BUSINESS);
        phone.setUser(ceo);
        createPhones(phones, "444-444-234", PhoneType.PRIVATE, ceo);
        phones.add(phone);

        phoneRepository.save(phones);
        userRepository.save(ceo);

    }

    @Override
    public User findUser(Long id) {

        Optional<User> employee = Optional.ofNullable(userRepository.findOne(id));
        return employee.orElseThrow(() -> new NotFoundException("There is no employee with id: " + id));
    }

    @Override
    public List<User> findAllEmployeesOfDepartment(Long idDep) {

        Department department= departmentService.findOne(idDep);
        Optional<List<User>> employees = Optional.ofNullable(userRepository.findAllEmployeesOfDepartment(department.getId()));
        return employees.orElseThrow(() -> new NotFoundException("There is no employees in this department: " + department.getId()));
    }

    @Override
    public List<User> findAll() {

        return userRepository.findAll();
    }

    public void createPhones(Set<Phone> phones, String number, PhoneType phoneType, User user) {
        Phone phone = new Phone();
        phone.setNumber(number);
        phone.setType(phoneType);
        phone.setUser(user);
        phones.add(phone);
    }

    @Override
    public void createEmployee(EmployeeDTO employeeDTO) {
        User employee = new User();
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getFirstName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setHead(employeeDTO.isHead());
        employee.setActive(true);
        employee.setSalary(employeeDTO.getSalary());
        employee.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        if (employee.isHead()) {
            Role userRole = roleRepository.findByName("ROLE_HEAD");
            employee.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        }
        Role userRole = roleRepository.findByName("ROLE_EMPLOYEE");
        employee.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        employee.setDateOfEmployment(LocalDate.now());
        Department department= departmentService.findOne(employeeDTO.getDepartmentId());
        employee.setDepartment(department);

        Set<Phone> phones = new HashSet<>();
        phoneRepository.save(phones);
        userRepository.save(employee);

        departmentService.addEmployee(department.getId(),employee);
        departmentService.updateDepartment(department);
    }

    @Override
    public void updatePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        updateUser(user);
        userRepository.save(user);
    }


    @Override
    public void delete(User employee) {
        Department department = employee.getDepartment();
        departmentService.removeEmployee(department.getId(),employee);
        userRepository.delete(employee);

    }

    @Override
    public User findByEmail(String email) {

        return userRepository.findByEmail(email);
    }

    @Override
    public void setLoginTime(Long userId) {

        userRepository.setLoginTime(userId);
    }

    @Override
    public void setSalary(User employee) {



        if (employee.getSalary().compareTo(employee.getDepartment().getMinSalary()) >= 0 && employee.getSalary().compareTo(employee.getDepartment().getMaxSalary()) <= 0) {
            employee.setSalary(employee.getSalary());
        } else { // automatically adjusted (to max)
            employee.setSalary(employee.getDepartment().getMaxSalary());
        }
    }

    @Override
    public void updateUser(User user) {
        user.setId(user.getId());
        user.setFirstName(user.getFirstName());
        user.setLastName(user.getLastName());
        user.setEmail(user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.isHead()) {
            Role userRole = roleRepository.findByName("ROLE_HEAD");
            user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        }
        user.setDepartment(user.getDepartment());
        user.setDateOfEmployment(user.getDateOfEmployment());
        user.setLastLoginTime(user.getLastLoginTime());
        user.setActive(user.isActive());
        user.setHead(user.isHead());
        user.setSalary(user.getSalary());
        userRepository.save(user);

    }

    @Override
    public void createRoles() {
        Role r1 = new Role();
        r1.setName("ROLE_CEO");
        Role r2 = new Role();
        r2.setName("ROLE_HEAD");
        Role r3 = new Role();
        r3.setName("ROLE_EMPLOYEE");
        roleRepository.save(r1);
        roleRepository.save(r2);
        roleRepository.save(r3);
    }

}
