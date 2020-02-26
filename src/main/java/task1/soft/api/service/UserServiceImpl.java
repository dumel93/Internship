package task1.soft.api.service;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import task1.soft.api.entity.Role;
import task1.soft.api.entity.User;
import task1.soft.api.repo.DepartmentRepository;
import task1.soft.api.repo.RoleRepository;
import task1.soft.api.repo.UserRepository;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserServiceImpl implements UserService {


    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DepartmentRepository departmentRepository;


    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Autowired
    private UserRepository userRepository;



    public UserServiceImpl(UserRepository repository) {

        this.userRepository=repository;

    }


    @Override
    public void saveCEO() {

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
    public List<User> findAllEmployyesOfDep(Long idDep) {
        return userRepository.findAllEmployyesOfDep(idDep);
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
    public void saveEmployee() {
        User employee = new User();

        employee.setFirstName("w");
        employee.setLastName("w");
        employee.setEmail("cwwww@pgs.com");
        employee.setActive(true);
        employee.setPassword(passwordEncoder.encode("admin123"));
        Role userRole = roleRepository.findByName("ROLE_EMPLOYEE");
        employee.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        employee.setDepartaments( departmentRepository.findOne(1L));
        userRepository.save(employee);
    }


    @Override
    public void update(User employee, Long id) {
        employee.setUserId(id);
        userRepository.save(employee);
    }

    @Override
    public void createRoles() {
        Role r1= new Role();
        r1.setName("ROLE_CEO");
        Role r2= new Role();
        r2.setName("ROLE_HEAD");
        Role r3= new Role();
        r3.setName("ROLE_EMPLOYEE");
        roleRepository.save(r1);
        roleRepository.save(r2);
        roleRepository.save(r3);
    }


}
