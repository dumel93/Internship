package task1.soft.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import task1.soft.api.entity.Role;
import task1.soft.api.entity.User;
import task1.soft.api.repo.RoleRepository;
import task1.soft.api.repo.UserRepository;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;


@Service
@Transactional
public class UserServiceImpl implements UserService {


    @Autowired
    private RoleRepository roleRepository;



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
        ceo.setPassword("admin123");
        ceo.setPassword(passwordEncoder.encode(ceo.getPassword()));
        Role userRole = roleRepository.findByName("ROLE_CEO");
        ceo.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        userRepository.save(ceo);
    }

    @Override
    public void findAllUsers() {
        userRepository.findAll();
    }
}
