package task1.soft.api.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import task1.soft.api.entity.*;
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
        ceo.setRoles(new HashSet<Role>(Arrays.asList(userRole)));

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
        return userRepository.findOne(id);
    }

    @Override
    public List<User> findAllEmployeesOfDepartment(Long idDep) {
        return userRepository.findAllEmployeesOfDepartment(idDep);
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
    public void createEmployee(String firstName, String lastName, String email, String password, BigDecimal salary, Department department) {
        User employee = new User();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employee.setActive(true);
        employee.setSalary(salary);
        employee.setPassword(passwordEncoder.encode(password));
        if (employee.isHead()) {
            Role userRole = roleRepository.findByName("ROLE_HEAD");
            employee.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        }
        Role userRole = roleRepository.findByName("ROLE_EMPLOYEE");
        employee.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        employee.setDateOfEmployment(LocalDate.now());
        employee.setDepartment(department);

        Set<Phone> phones = new HashSet<>();
        phoneRepository.save(phones);
        userRepository.save(employee);

        department.getEmployees().add(employee);
        departmentService.updateDepartment(department);
    }

    @Override
    public void updatePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        updateUser(user);
        userRepository.save(user);
    }


    @Override
    public boolean isEmailExist(User employee) throws UserExistsException {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getEmail().equals(employee.getEmail())) {
                throw new UserExistsException("Unable to create. A User with email {} already exist" + employee.getEmail());
            }
        }
        return false;
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
