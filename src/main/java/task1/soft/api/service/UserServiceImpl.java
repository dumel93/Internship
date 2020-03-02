package task1.soft.api.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import task1.soft.api.entity.*;
import task1.soft.api.repo.DepartmentRepository;
import task1.soft.api.repo.PhoneRepository;
import task1.soft.api.repo.RoleRepository;
import task1.soft.api.repo.UserRepository;

import javax.transaction.Transactional;
import java.util.*;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final RoleRepository roleRepository;

    private final DepartmentRepository departmentRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final PhoneRepository phoneRepository;

    private final DepartmentService departmentService;

    @Autowired
    public UserServiceImpl(RoleRepository roleRepository, DepartmentRepository departmentRepository, BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, PhoneRepository phoneRepository, DepartmentService departmentService) {
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.phoneRepository = phoneRepository;
        this.departmentService = departmentService;
    }


    @Override
    public void setupCEO() {
        User ceo = new User();
        ceo.setFirstName("admin");
        ceo.setLastName("admin");
        ceo.setEmail("ceo@pgs.com");
        ceo.setSalary(100000d);
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
    public void findAllUsers() {
        userRepository.findAll();
    }

    @Override
    public List<User> findAllEmployeesOfDepartment(Long idDep) {
        return userRepository.findAllEmployeesOfDepartment(idDep);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(User employee) {
        userRepository.save(employee);
    }


    public Set<Phone> createPhones(Set<Phone> phones, String number, PhoneType phoneType, User user) {
        Phone phone = new Phone();
        phone.setNumber(number);
        phone.setType(phoneType);
        phone.setUser(user);
        phones.add(phone);
        return phones;
    }

    @Override
    public void createEmployee(String firstName, String lastName, String email, String password, Department department) {
        User employee = new User();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employee.setActive(true);
        employee.setSalary(200d);
        employee.setPassword(passwordEncoder.encode(password));
        Role userRole = roleRepository.findByName("ROLE_EMPLOYEE");
        employee.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        employee.setDateOfEmployment(new Date());
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
    public boolean isUserExist(User employee) {
        return false;
    }

    @Override
    public boolean isEmailExist(User employee) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getEmail().equals(employee.getEmail())) {
                return true;
            }
        }
        return false;
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
            user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
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
