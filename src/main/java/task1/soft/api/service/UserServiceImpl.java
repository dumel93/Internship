package task1.soft.api.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import task1.soft.api.entity.*;
import task1.soft.api.repo.DepartmentRepository;
import task1.soft.api.repo.PhoneRepository;
import task1.soft.api.repo.RoleRepository;
import task1.soft.api.repo.UserRepository;
import task1.soft.api.util.DepartmentSearchQueryCriteriaConsumer;
import task1.soft.api.util.SearchCriteria;
import task1.soft.api.util.UserSearchQueryCriteriaConsumer;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;


@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final PhoneRepository phoneRepository;

    private final DepartmentService departmentService;

    private final DepartmentRepository departmentRepository;

    private final EntityManager entityManager;

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

    public Set<Phone> createPhones(Set<Phone> phones, String number, PhoneType phoneType, User user) {
        Phone phone = new Phone();
        phone.setNumber(number);
        phone.setType(phoneType);
        phone.setUser(user);
        phones.add(phone);
        return phones;
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
            employee.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        }
        Role userRole = roleRepository.findByName("ROLE_EMPLOYEE");
        employee.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
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
    public void delete(User employee) {
        Department department= employee.getDepartment();
        department.removeEmployee(employee);
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
    public User findHeadByIdDepart(Long id) {
        return userRepository.findHeadByIdDepart(id);
    }

    @Override
    public List<User> searchEmployee(List<SearchCriteria> params) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root r = query.from(User.class);
        Predicate predicate = builder.conjunction();

        UserSearchQueryCriteriaConsumer searchConsumer =
                new UserSearchQueryCriteriaConsumer(predicate, builder, r);
        params.stream().forEach(searchConsumer);
        predicate = searchConsumer.getPredicate();
        query.where(predicate);

        List<User> result = entityManager.createQuery(query).getResultList();
        return result;
    }

    @Override
    public List<User> findAll(Integer offset, Integer limit, String sortBy, String orderBy) {
        if (orderBy.toUpperCase().equals("DESC")) {
            Sort sortdesc = new Sort(new Sort.Order(Sort.Direction.DESC, sortBy));
            Pageable pageable = new PageRequest(offset, limit, sortdesc);
            return userRepository.findAll(pageable).getContent();
        }
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, sortBy));
        Pageable pageable = new PageRequest(offset, limit, sort);
        return userRepository.findAll(pageable).getContent();
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
