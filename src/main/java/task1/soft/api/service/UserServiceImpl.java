package task1.soft.api.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import task1.soft.api.dto.EmployeeDTO;
import task1.soft.api.entity.*;
import task1.soft.api.exception.NotFoundException;
import task1.soft.api.repo.PhoneRepository;
import task1.soft.api.repo.RoleRepository;
import task1.soft.api.repo.UserRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;


@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private RoleRepository roleRepository;

    private BCryptPasswordEncoder passwordEncoder;

    private UserRepository userRepository;

    private PhoneRepository phoneRepository;

    private DepartmentService departmentService;
    
    @Override
    public User setupCEO() {
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
        Phone phone2 = new Phone();
        phone2.setNumber("507-201-020");
        phone2.setType(PhoneType.PRIVATE);
        phone2.setUser(ceo);
        phones.add(phone);
        phones.add(phone2);
        phoneRepository.save(phones);
        userRepository.save(ceo);
        return ceo;

    }

    @Override
    public User findEmployee(Long id) throws NotFoundException {

        Optional<User> employee = Optional.ofNullable(userRepository.findOne(id));
        return employee.orElseThrow(() -> new NotFoundException("There is no employee with id: " + id));
    }

    @Override
    public List<User> findAllEmployeesOfDepartment(Long idDep) throws NotFoundException {

        Department department = departmentService.findDepartment(idDep);
        Optional<List<User>> employees = Optional.ofNullable(userRepository.findAllEmployeesOfDepartment(department.getId()));
        return employees.orElseThrow(() -> new NotFoundException("There is no employees in this department: " + department.getId()));

    }

    @Override
    public List<User> findAllEmployees() {

        return userRepository.findAll();
    }


    @Override
    public User createEmployee(EmployeeDTO employeeDTO) {
        User user = new User();
        user.setFirstName(employeeDTO.getFirstName());
        user.setLastName(employeeDTO.getLastName());
        user.setEmail(employeeDTO.getEmail());
        user.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        Department department = departmentService.findDepartment(employeeDTO.getDepartmentId());
        user.setDepartment(department);
        user.setHead(employeeDTO.isHead());
        User head = departmentService.findHeadByIdDepart(department.getId());
        if (head != null) {
            head.setHead(false);
            userRepository.save(head);
        }

        if (employeeDTO.isHead()) {
            Role userRole = roleRepository.findByName("ROLE_HEAD");
            user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        }else {
            Role userRole = roleRepository.findByName("ROLE_EMPLOYEE");
            user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        }

        user.setDateOfEmployment(employeeDTO.getDateOfEmployment());
        user.setLastLoginTime(employeeDTO.getLastLoginTime());
        user.setActive(true);
        user.setSalary(employeeDTO.getSalary());
        this.setSalary(user);
//        Set<Phone> phones = employeeDTO.getPhones();
//        for (Phone phone : phones) {
//            phone.setUser(user);
//        }
//        phoneRepository.save(phones);
        userRepository.save(user);
        return user;
    }

    @Override
    public void updateEmployee(EmployeeDTO employeeDTO) {
        User user = this.findEmployee(employeeDTO.getId());
        user.setId(employeeDTO.getId());
        userRepository.save(user);

    }

    @Override
    public void deleteEmployee(User employee) {
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
    public boolean isUserInHeadDepart(User user, Department headDepartment) throws NotFoundException{

        if (this.findAllEmployeesOfDepartment(headDepartment.getId()).contains(user)) {
            return true;
        } else
            throw new NotFoundException(" this user " + user.toString() + " is not in this department of head:" + headDepartment);
    }


    @Override
    public void setSalary(User employee) {


        if (employee.getSalary().compareTo(employee.getDepartment().getMinSalary()) >= 0 && employee.getSalary().compareTo(employee.getDepartment().getMaxSalary()) <= 0) {
            employee.setSalary(employee.getSalary());
        } else {
            if (employee.getSalary().compareTo(employee.getDepartment().getMinSalary()) < 0) {
                // automatically adjusted (to min)
                employee.setSalary(employee.getDepartment().getMinSalary());
            } else {
                // automatically adjusted (to max)
                employee.setSalary(employee.getDepartment().getMaxSalary());
            }

        }
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
