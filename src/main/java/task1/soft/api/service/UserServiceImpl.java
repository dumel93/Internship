package task1.soft.api.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import task1.soft.api.entity.Phone;
import task1.soft.api.entity.PhoneType;
import task1.soft.api.entity.Role;
import task1.soft.api.entity.User;
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

    @Autowired
    public UserServiceImpl(RoleRepository roleRepository, DepartmentRepository departmentRepository, BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, PhoneRepository phoneRepository) {
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.phoneRepository = phoneRepository;
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

        Set<Phone> phones= new HashSet<>();
        Phone phone= new Phone();
        phone.setNumber("533-202-020");
        phone.setType(PhoneType.BUSINESS);
        phone.setUser(ceo);
        createPhones(phones,"444-444-234",PhoneType.PRIVATE,ceo);
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


    public Set<Phone> createPhones(Set<Phone> phones, String number, PhoneType phoneType,User user) {
        Phone phone = new Phone();
        phone.setNumber(number);
        phone.setType(phoneType);
        phone.setUser(user);
        phones.add(phone);
        return phones;
    }

    @Override
    public void createEmployee(String firstName, String lastName, String email, String password) {
        User employee = new User();


        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employee.setActive(true);
        employee.setPassword(passwordEncoder.encode(password));
        Role userRole = roleRepository.findByName("ROLE_EMPLOYEE");
        employee.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        employee.setDateOfEmployment(new Date());
        employee.setDepartment(departmentRepository.findOne(1L));
        Set<Phone> phones = new HashSet<>();
        phoneRepository.save(phones);
        userRepository.save(employee);


    }

    @Override
    public void updatePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        updateUser(user);
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        user.setId(user.getId());
        user.setFirstName(user.getFirstName());
        user.setLastName(user.getLastName());
        user.setEmail(user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName("ROLE_EMPLOYEE");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        user.setDepartment(user.getDepartment());
        user.setDateOfEmployment(user.getDateOfEmployment());
        user.setLoginTime(user.getLoginTime());
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
