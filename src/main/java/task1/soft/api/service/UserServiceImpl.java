package task1.soft.api.service;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import task1.soft.api.entity.Phone;
import task1.soft.api.entity.Role;
import task1.soft.api.entity.User;
import task1.soft.api.repo.DepartmentRepository;
import task1.soft.api.repo.PhoneRepository;
import task1.soft.api.repo.RoleRepository;
import task1.soft.api.repo.UserRepository;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;



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
        ceo.setSalary(100000f);
        ceo.setActive(true);
        ceo.setPassword(passwordEncoder.encode("admin123"));
        Role userRole = roleRepository.findByName("ROLE_CEO");
        ceo.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        userRepository.save(ceo);

    }

    @Override
    public void findAllUsers() {
        userRepository.findAll();
    }



    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(User employee) {
        userRepository.save(employee);
    }

    @Override
    public void createEmployee() {
        User employee = new User();

//        Phone phone1= new Phone();
//        phone1.setId(1L);
//        phone1.setNumber("5332073955");
//        phone1.setType("private");
//
//        Phone phone2= new Phone();
//        phone1.setId(2L);
//        phone2.setNumber("533443955");
//        phone2.setType("company");
//
//
//        phoneRepository.save(phone1);
//        phoneRepository.save(phone2);
//        Set<Phone> phones = new HashSet<>();
//        phones.add(phone1);
//        phones.add(phone2);

//        employee.setPhones(phones);
        employee.setFirstName("Witold");
        employee.setLastName("Marzec");
        employee.setEmail("cde@pgs.com");
        employee.setActive(true);
        employee.setPassword(passwordEncoder.encode("haslo123"));
        Role userRole = roleRepository.findByName("ROLE_EMPLOYEE");
        employee.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        employee.setdepartment(departmentRepository.findOne(1L));
        userRepository.save(employee);


        User empl2 = new User();
        empl2.setFirstName("Karol");
        empl2.setLastName("Maj");
        empl2.setEmail("abc@pgs.com");
        empl2.setActive(true);
        empl2.setPassword(passwordEncoder.encode("haslo123"));
        Role role = roleRepository.findByName("ROLE_EMPLOYEE");
        empl2.setRoles(new HashSet<Role>(Arrays.asList(role)));
        empl2.setdepartment(departmentRepository.findOne(1L));
        userRepository.save(empl2);


    }

    @Override
    public void updateUser(User employee, Long id) {
        employee.setId(id);
        userRepository.save(employee);
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
