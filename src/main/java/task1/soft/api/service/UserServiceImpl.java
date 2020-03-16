package task1.soft.api.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import task1.soft.api.dto.EmployeeDTO;
import task1.soft.api.dto.EmployeeReadDTO;
import task1.soft.api.dto.PhoneDTO;
import task1.soft.api.entity.*;
import task1.soft.api.exception.NotFoundException;
import task1.soft.api.repo.PhoneRepository;
import task1.soft.api.repo.RoleRepository;
import task1.soft.api.repo.UserRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private RoleRepository roleRepository;

    private BCryptPasswordEncoder passwordEncoder;

    private UserRepository userRepository;

    private PhoneRepository phoneRepository;

    private DepartmentService departmentService;

    private ModelMapper modelMapper;

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
        User user = this.getUserBasicDetails(employeeDTO);
        Department department = departmentService.findDepartment(employeeDTO.getDepartmentId());
        user.setDepartment(department);
        this.setHead(employeeDTO, user, department);
        this.setSalary(user);
        this.setActivity(user);
        this.setPhones(employeeDTO, user);
        userRepository.save(user);
        return user;
    }

    @Override
    public User updateEmployee(EmployeeDTO employeeDTO) {
        User user = this.getUserBasicDetails(employeeDTO);
        Department department = departmentService.findDepartment(employeeDTO.getDepartmentId());
        user.setDepartment(department);
        this.setHead(employeeDTO, user, department);
        this.setSalary(user);
        this.setPhones(employeeDTO, user);
        user.setActive(employeeDTO.isActive());
        this.setActivity(user);
        user.setId(employeeDTO.getId());
        userRepository.save(user);
        return user;

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
    public boolean isUserInHeadDepart(User user, Department headDepartment) throws NotFoundException {

        if (this.findAllEmployeesOfDepartment(headDepartment.getId()).contains(user)) {
            return true;
        } else
            throw new NotFoundException("This user is not working at this department (head Department)..");
    }

    @Override
    public EmployeeReadDTO getDataFromEmployeeReadDTO(EmployeeReadDTO employeeReadDTO) {

        User user = this.findEmployee(employeeReadDTO.getId());
        if (employeeReadDTO.getDepartmentId() != null) {
            employeeReadDTO.setNameOfDepartment(departmentService.findDepartment(employeeReadDTO.getDepartmentId()).getName());
            User head = departmentService.findHeadByIdDepart(employeeReadDTO.getDepartmentId());
            if (head != null) {
                EmployeeReadDTO headDTO = modelMapper.map(head, EmployeeReadDTO.class);
                headDTO.setDepartmentHeadProfile(null);
                employeeReadDTO.setDepartmentHeadProfile(headDTO);
            }
        }
        List<Phone> phones = user.getPhones();
        if (phones != null) {
            List<PhoneDTO> phoneDTOS = phones.stream().map(phone -> modelMapper.map(phone, PhoneDTO.class)).collect(Collectors.toList());
            phoneDTOS.forEach(phone -> employeeReadDTO.getPhones().add(phone));

        }

        return employeeReadDTO;
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

    @Override
    public boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException {

        if (!fieldName.equals("email")) {
            throw new UnsupportedOperationException("This email already exists");
        }

        if (value == null) {
            return false;
        }
        return this.userRepository.existsByEmail(value.toString());
    }


    private User getUserBasicDetails(EmployeeDTO employeeDTO) {
        User user = new User();
        user.setFirstName(employeeDTO.getFirstName());
        user.setLastName(employeeDTO.getLastName());
        user.setEmail(employeeDTO.getEmail());
        user.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        user.setActive(true);
        user.setSalary(employeeDTO.getSalary());
        return user;
    }

    @Override
    public void setPhones(EmployeeDTO employeeDTO, User user) {
        List<PhoneDTO> phoneDTOS = employeeDTO.getPhones();
        if (phoneDTOS != null) {
            List<Phone> phones = phoneDTOS.stream()
                    .map(phone -> modelMapper.map(phone, Phone.class))
                    .collect(Collectors.toList());
            phones.forEach(phone -> phone.setUser(user));
            user.setPhones(phones);
            phoneRepository.save(phones);
        }
    }

    @Override
    public void setHead(EmployeeDTO employeeDTO, User user, Department department) {
        Optional<User> lastHead = Optional.ofNullable(departmentService.findHeadByIdDepart(department.getId()));
        user.setHead(employeeDTO.isHead());
        if (lastHead.isPresent() && employeeDTO.isHead()) {
            User lastH = lastHead.get();
            lastH.setHead(false);
            Role userRole = roleRepository.findByName("ROLE_HEAD");
            Role userRoleChanged = roleRepository.findByName("ROLE_EMPLOYEE");
            lastH.setRoles(new HashSet<>(Collections.singletonList(userRoleChanged)));
            user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
            userRepository.save(lastH);

        } else if (!lastHead.isPresent() && employeeDTO.isHead()) {
            Role userRole = roleRepository.findByName("ROLE_HEAD");
            user.setRoles(new HashSet<>(Collections.singletonList(userRole)));

        } else {
            Role userRole = roleRepository.findByName("ROLE_EMPLOYEE");
            user.setRoles(new HashSet<>(Collections.singletonList(userRole)));

        }
    }

    @Override
    public void setActivity(User employee) {

        if (!employee.isActive()) {
            employee.getRoles().clear();
            employee.setActive(false);
            employee.setHead(false);
        } else {
            employee.getRoles().clear();
            employee.getRoles().add(roleRepository.findByName("ROLE_EMPLOYEE"));
            employee.setActive(true);
        }
    }

}
