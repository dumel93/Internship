package task1.soft.api.service;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import task1.soft.api.dto.EmployeeDTO;
import task1.soft.api.dto.EmployeeReadDTO;
import task1.soft.api.dto.PhoneDTO;
import task1.soft.api.entity.*;
import task1.soft.api.exception.NotFoundException;
import task1.soft.api.repo.PhoneRepository;
import task1.soft.api.repo.RoleRepository;
import task1.soft.api.repo.UserRepository;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PhoneRepository phoneRepository;
    @Mock
    private DepartmentService departmentService;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private UserService userService = new UserServiceImpl(roleRepository, passwordEncoder, userRepository, phoneRepository, departmentService, modelMapper);


    @Test
    public void testSetupCEO() {
        //	given

        User ceo = new User();
        ceo.setFirstName("admin");
        ceo.setLastName("admin");
        ceo.setEmail("ceo@pgs.com");
        ceo.setSalary(new BigDecimal("20000"));
        ceo.setActive(true);
        Role userRole = new Role();
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(userRole);
        userRole.setName("ROLE_CEO");
        ceo.setRoles(roleSet);
        ceo.setPassword("$2a$10$qSsKGiXLCQnHwuczS/M.HuOaNteaFpDT6rzCAfOXdDIbgu8ta4CFe");
        Set<Phone> phones = new HashSet<>();
        Phone phone = new Phone();
        phone.setNumber("533-202-020");
        phone.setType(PhoneType.BUSINESS);
        phone.setUser(ceo);
        phones.add(phone);

        when(passwordEncoder.encode("admin123")).thenReturn("$2a$10$qSsKGiXLCQnHwuczS/M.HuOaNteaFpDT6rzCAfOXdDIbgu8ta4CFe");
        when(roleRepository.findByName("ROLE_CEO")).thenReturn(userRole);
        when(userRepository.save(ceo)).thenReturn(ceo);
        when(phoneRepository.save(phone)).thenReturn(phone);
        // 	when

        User result = userService.setupCEO();
        // 	then
        assertNotNull(result);
        assertEquals(result, ceo);
        assertEquals(ceo.getFirstName(), result.getFirstName());
        assertEquals(ceo.getPassword(), result.getPassword());
        assertEquals(ceo.getSalary(), result.getSalary());
        assertEquals(ceo.getId(), result.getId());

    }

    @Test
    public void testFindEmployee() {
        //	given
        User user = new User();
        user.setFirstName("d");
        user.setLastName("k");
        user.setEmail("i@wp.pl");
        user.setSalary(new BigDecimal("1000"));


        when(userRepository.findOne(1L)).thenReturn(user);
        // 	when
        User result = userRepository.findOne(1L);
        // 	then
        assertEquals(user.getId(), result.getId());
        assertEquals(result.getFirstName(), "d");
    }

    @Test(expected = NotFoundException.class)
    public void testFindEmployeeFail() {
        //	given
        when(userRepository.findOne(3L)).thenThrow(NotFoundException.class);
        // 	when
        userService.findEmployee(3L);
    }

    @Test
    public void testFindAllEmployeesOfDepartment() {
        Department department = new Department();
        department.setName("it");
        department.setCity("rzeszow");
        department.setId(1L);

        User user = new User();
        user.setFirstName("d");
        user.setLastName("k");
        user.setEmail("i@wp.pl");
        user.setSalary(new BigDecimal("1000"));


        User user2 = new User();
        user2.setFirstName("d2");
        user2.setLastName("k2");
        user2.setEmail("i2@wp.pl");
        user2.setHead(true);
        user2.setSalary(new BigDecimal("2000"));


        user.setDepartment(department);
        user2.setDepartment(department);
        department.getEmployees().add(user);
        department.getEmployees().add(user2);
        List<User> users = Arrays.asList(user, user2);


        when(departmentService.findDepartment(department.getId())).thenReturn(department);
        when(userRepository.findAllEmployeesOfDepartment(department.getId())).thenReturn(users);


        //when
        List<User> allEmployeesOfDepartment = userService.findAllEmployeesOfDepartment(1L);
        // 	then
        assertEquals(allEmployeesOfDepartment.size(), 2);
        assertEquals(allEmployeesOfDepartment.get(0), user);
        assertEquals(allEmployeesOfDepartment.get(1), user2);

    }

    @Test
    public void testFindAllEmployees() {

        //given
        User user = new User();
        user.setFirstName("d");
        user.setLastName("k");
        user.setEmail("i@wp.pl");
        user.setSalary(new BigDecimal("1000"));


        User user2 = new User();
        user2.setFirstName("d2");
        user2.setLastName("k2");
        user2.setEmail("i2@wp.pl");
        user2.setHead(true);
        user2.setSalary(new BigDecimal("2000"));

        List<User> users = Arrays.asList(user, user2);
        when(userRepository.findAll()).thenReturn(users);
        // 	when
        List<User> result = userService.findAllEmployees();
        // 	then
        assertEquals(result.size(), 2);
        assertEquals(result.get(0), user);
        assertEquals(result.get(1), user2);

    }

    @Test
    public void testUpdateEmployee() {
        //	given

        Department department = new Department();
        department.setName("it");
        department.setCity("rzeszow");
        department.setMinSalary(BigDecimal.valueOf(1000));
        department.setMaxSalary(BigDecimal.valueOf(2000));

        User user = new User();
        user.setFirstName("d");
        user.setLastName("k");
        user.setEmail("i@wp.pl");
        user.setSalary(new BigDecimal("1000"));
        user.setPassword("$2a$10$qSsKGiXLCQnHwuczS/M.HuOaNteaFpDT6rzCAfOXdDIbgu8ta4CFe");
        user.setActive(true);
        Role userRole = roleRepository.findByName("ROLE_HEAD");
        user.setRoles(new HashSet<>(Collections.singletonList(userRole)));

        user.setDepartment(department);

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setPassword(user.getPassword());
        employeeDTO.setEmail(user.getEmail());
        employeeDTO.setFirstName(user.getFirstName());
        employeeDTO.setLastName(user.getLastName());
        employeeDTO.setActive(user.isActive());
        employeeDTO.setDepartmentId(1L);
        employeeDTO.setSalary(user.getSalary());

        User head = new User();
        head.setDepartment(department);

        when(userRepository.findOne(1L)).thenReturn(user);
        when(departmentService.findDepartment(employeeDTO.getDepartmentId())).thenReturn(department);
        when(passwordEncoder.encode("admin123")).thenReturn("$2a$10$qSsKGiXLCQnHwuczS/M.HuOaNteaFpDT6rzCAfOXdDIbgu8ta4CFe");
        when(roleRepository.findByName("ROLE_HEAD")).thenReturn(userRole);
        when(departmentService.findHeadByIdDepart(employeeDTO.getDepartmentId())).thenReturn(head);
        when(userService.findEmployee(1L)).thenReturn(user);
        //when
        employeeDTO.setFirstName("test");
        User result = userService.updateEmployee(employeeDTO);
        // 	then

        assertEquals(result.getFirstName(), "test");
        assertEquals(result.getEmail(), user.getEmail());

    }

    @Test
    public void testCreateRoles() {

        Role role1 = new Role();
        role1.setName("ROLE_CEO");
        Role role2 = new Role();
        role2.setName("ROLE_HEAD");
        Role role3 = new Role();
        role3.setName("ROLE_EMPLOYEE");

        when(roleRepository.save(role1)).thenReturn(role1);
        when(roleRepository.save(role2)).thenReturn(role2);
        when(roleRepository.save(role3)).thenReturn(role3);

        // 	when
        userService.createRoles();
        // 	then
        verify(roleRepository, times(1)).save(role1);
        verify(roleRepository, times(1)).save(role2);
        verify(roleRepository, times(1)).save(role3);


    }

    @Test
    public void testCreateEmployee() {
        Department department = new Department();
        department.setName("it");
        department.setCity("rzeszow");
        department.setMinSalary(BigDecimal.valueOf(1000));
        department.setMaxSalary(BigDecimal.valueOf(2000));

        User user = new User();
        user.setFirstName("d");
        user.setLastName("k");
        user.setEmail("i@wp.pl");
        user.setSalary(new BigDecimal("1000"));
        user.setPassword("$2a$10$qSsKGiXLCQnHwuczS/M.HuOaNteaFpDT6rzCAfOXdDIbgu8ta4CFe");
        user.setActive(true);
        Role userRole = roleRepository.findByName("ROLE_HEAD");
        user.setRoles(new HashSet<>(Collections.singletonList(userRole)));

        user.setDepartment(department);

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setPassword("admin123");
        employeeDTO.setEmail(user.getEmail());
        employeeDTO.setFirstName(user.getFirstName());
        employeeDTO.setLastName(user.getLastName());
        employeeDTO.setActive(user.isActive());
        employeeDTO.setDepartmentId(1L);
        employeeDTO.setSalary(user.getSalary());


        User head = new User();
        head.setDepartment(department);

        when(userRepository.findOne(1L)).thenReturn(user);
        when(departmentService.findDepartment(employeeDTO.getDepartmentId())).thenReturn(department);
        when(passwordEncoder.encode("admin123")).thenReturn("$2a$10$qSsKGiXLCQnHwuczS/M.HuOaNteaFpDT6rzCAfOXdDIbgu8ta4CFe");
        when(roleRepository.findByName("ROLE_HEAD")).thenReturn(userRole);
        when(departmentService.findHeadByIdDepart(employeeDTO.getDepartmentId())).thenReturn(head);
        when(userService.findEmployee(1L)).thenReturn(user);

        User result = userService.createEmployee(employeeDTO);
        // 	then

        assertEquals(result, user);
        assertEquals(result.getEmail(), user.getEmail());
        assertEquals(result.getFirstName(), user.getFirstName());

    }


    @Test
    public void testDeleteEmployee() {

        //given
        Department department = new Department();
        department.setName("it");
        department.setCity("rzeszow");

        User user = new User();
        user.setFirstName("d");
        user.setLastName("k");
        user.setEmail("i@wp.pl");
        user.setSalary(new BigDecimal("1000"));

        department.getEmployees().add(user);

        when(departmentService.findDepartment(1L)).thenReturn(department);


        Mockito.doNothing().when(userRepository).delete(1L);
        // 	when
        userService.deleteEmployee(user);
        // 	then
        verify(userRepository, times(1)).delete(user);

    }

    @Test
    public void testDeleteEmployeeFail() {

        User user = new User();
        user.setFirstName("d");
        user.setLastName("k");
        user.setEmail("i@wp.pl");
        user.setSalary(new BigDecimal("1000"));
        Mockito.doNothing().when(userRepository).delete(1L);
        // 	when
        userService.deleteEmployee(user);
        // 	then
        verify(userRepository, times(1)).delete(user);

    }

    @Test
    public void testFindByEmail() {
        User user = new User();
        user.setFirstName("d");
        user.setLastName("k");
        user.setEmail("i@wp.pl");
        user.setSalary(new BigDecimal("1000"));
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        // 	when
        User result = userRepository.findByEmail("i@wp.pl");
        // 	then
        assertEquals(user.getId(), result.getId());
        assertEquals(result.getFirstName(), "d");

    }

    @Test
    public void testSetSalary() {
        Department department = new Department();
        department.setName("it");
        department.setCity("rzeszow");
        department.setMinSalary(BigDecimal.valueOf(1000));
        department.setMaxSalary(BigDecimal.valueOf(2000));

        User user = new User();
        user.setFirstName("d");
        user.setLastName("k");
        user.setEmail("i@wp.pl");
        user.setSalary(BigDecimal.valueOf(1500));
        user.setDepartment(department);
        userService.setSalary(user);
        assertEquals(user.getSalary(), BigDecimal.valueOf(1500));

        user.setSalary(BigDecimal.valueOf(500));
        userService.setSalary(user);
        assertEquals(user.getSalary(), BigDecimal.valueOf(1000));

        user.setSalary(BigDecimal.valueOf(2500));
        userService.setSalary(user);
        assertEquals(user.getSalary(), BigDecimal.valueOf(2000));

    }

    @Test
    public void testIsUserInHeadDepart() {

        Department department = new Department();
        department.setName("it");
        department.setCity("rzeszow");
        department.setId(1L);

        User user = new User();
        user.setFirstName("d");
        user.setLastName("k");
        user.setEmail("i@wp.pl");
        user.setSalary(new BigDecimal("1000"));
        user.setDepartment(department);

        User user2 = new User();
        user2.setFirstName("d2");
        user2.setLastName("k2");
        user2.setEmail("i2@wp.pl");
        user2.setHead(true);
        user2.setSalary(new BigDecimal("2000"));
        user2.setDepartment(department);

        List<User> users = Arrays.asList(user, user2);

        when(departmentService.findDepartment(1L)).thenReturn(department);
        when(userRepository.findAllEmployeesOfDepartment(1L)).thenReturn(users);

        boolean result = userService.isUserInHeadDepart(user2, department);

        Assert.assertTrue(result);

    }

    @Test
    public void testGetDataFromEmployeeReadDTO() {

        Department department = new Department();
        department.setName("it");
        department.setCity("rzeszow");
        department.setId(1L);

        User user = new User();
        user.setFirstName("d");
        user.setLastName("k");
        user.setEmail("i@wp.pl");
        user.setId(1L);
        user.setSalary(new BigDecimal("1000"));
        user.setDepartment(department);
        user.setHead(true);

        EmployeeReadDTO employeeReadDTO = new EmployeeReadDTO();
        employeeReadDTO.setId(user.getId());
        employeeReadDTO.setEmail(user.getEmail());
        employeeReadDTO.setFirstName(user.getFirstName());
        employeeReadDTO.setLastName(user.getLastName());
        employeeReadDTO.setSalary(user.getSalary());
        employeeReadDTO.setDepartmentId(department.getId());
        employeeReadDTO.setHead(true);

        Phone phone = new Phone();
        phone.setNumber("222333444");
        phone.setType(PhoneType.BUSINESS);
        phone.setUser(user);
        PhoneDTO phoneDTO = new PhoneDTO();
        phoneDTO.setNumber("222333444");
        phoneDTO.setType(PhoneType.BUSINESS);
        List<PhoneDTO> phoneDTOList = Collections.singletonList(phoneDTO);
        employeeReadDTO.setPhones(phoneDTOList);

        when(userRepository.findOne(1L)).thenReturn(user);
        when(userService.findEmployee(1L)).thenReturn(user);
        when(departmentService.findDepartment(1L)).thenReturn(department);
        when(departmentService.findHeadByIdDepart(employeeReadDTO.getDepartmentId())).thenReturn(user);
        when(modelMapper.map(user, EmployeeReadDTO.class)).thenReturn(employeeReadDTO);
        when(modelMapper.map(phone, PhoneDTO.class)).thenReturn(phoneDTO);

        EmployeeReadDTO result = userService.getDataFromEmployeeReadDTO(employeeReadDTO);
        assertEquals(result.getNameOfDepartment(), "it");
        assertEquals(result.getDepartmentHeadProfile(), employeeReadDTO);
        assertEquals(result.getPhones(), phoneDTOList);

    }

    @Test
    public void testSetHead() {

        Department department = new Department();
        department.setName("it");
        department.setCity("rzeszow");
        department.setId(1L);

        User user = new User();
        user.setFirstName("d");
        user.setLastName("k");
        user.setEmail("i@wp.pl");
        user.setSalary(new BigDecimal("1000"));
        user.setDepartment(department);

        User user2 = new User();
        user2.setFirstName("d2");
        user2.setLastName("k2");
        user2.setEmail("i2@wp.pl");
        user2.setHead(true);
        user2.setSalary(new BigDecimal("2000"));
        user2.setDepartment(department);

    }

    @Test
    public void testSetActivity() {

        User user = new User();
        user.setFirstName("d");
        user.setLastName("k");
        user.setEmail("i@wp.pl");
        user.setSalary(BigDecimal.valueOf(1500));
        user.setActive(true);


        Role role = new Role();
        role.setName("ROLE_EMPLOYEE");
        Set<Role> roles = new HashSet<>();
        user.setRoles(roles);

        when(roleRepository.findByName("ROLE_EMPLOYEE")).thenReturn(role);
        userService.setActivity(user);
        assertTrue(user.isActive());
        assertEquals(user.getRoles().size(), 1);

        user.setActive(false);
        userService.setActivity(user);
        assertFalse(user.isActive());
        assertEquals(user.getRoles(), new HashSet<>());


    }

    @Test
    public void testSetPhones() {

        Department department = new Department();
        department.setName("it");
        department.setCity("rzeszow");
        department.setId(1L);

        User user = new User();
        user.setFirstName("d");
        user.setLastName("k");
        user.setEmail("i@wp.pl");
        user.setSalary(new BigDecimal("1000"));
        user.setDepartment(department);

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setPassword("admin123");
        employeeDTO.setEmail(user.getEmail());
        employeeDTO.setFirstName(user.getFirstName());
        employeeDTO.setLastName(user.getLastName());
        employeeDTO.setActive(user.isActive());
        employeeDTO.setDepartmentId(1L);
        employeeDTO.setSalary(user.getSalary());

        PhoneDTO phoneDTO = new PhoneDTO();
        phoneDTO.setNumber("222333444");
        phoneDTO.setType(PhoneType.BUSINESS);
        List<PhoneDTO> phonesDTO = Collections.singletonList(phoneDTO);

        employeeDTO.setPhones(phonesDTO);

        Phone phone = new Phone();
        phone.setNumber(phoneDTO.getNumber());
        phone.setType(phoneDTO.getType());

        List<Phone> phones = Collections.singletonList(phone);

        when(modelMapper.map(phoneDTO, Phone.class)).thenReturn(phone);
        userService.setPhones(employeeDTO, user);

        verify(phoneRepository, times(1)).save(phones);
    }
}
