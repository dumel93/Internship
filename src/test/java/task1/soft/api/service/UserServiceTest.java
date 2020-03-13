package task1.soft.api.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import task1.soft.api.dto.EmployeeDTO;
import task1.soft.api.entity.*;
import task1.soft.api.exception.NotFoundException;
import task1.soft.api.repo.PhoneRepository;
import task1.soft.api.repo.RoleRepository;
import task1.soft.api.repo.UserRepository;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
//
//    @Mock
//    private RoleRepository roleRepository;
//    @Mock
//    private BCryptPasswordEncoder passwordEncoder;
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private PhoneRepository phoneRepository;
//    @Mock
//    private DepartmentService departmentService;
//
//    @InjectMocks
//    private UserService userService = new UserServiceImpl(roleRepository,userRepository, phoneRepository, departmentService);
//
//
//
//
//    @Test
//    public void setupCEO() {
//        //	given
//
//        User ceo = new User();
//        ceo.setFirstName("admin");
//        ceo.setLastName("admin");
//        ceo.setEmail("ceo@pgs.com");
//        ceo.setSalary(new BigDecimal("20000"));
//        ceo.setActive(true);
//        Role userRole = new Role();
//        Set<Role> roleSet = new HashSet<>();
//        roleSet.add(userRole);
//        userRole.setName("ROLE_CEO");
//        ceo.setRoles(roleSet);
//        ceo.setPassword("$2a$10$qSsKGiXLCQnHwuczS/M.HuOaNteaFpDT6rzCAfOXdDIbgu8ta4CFe");
//        Set<Phone> phones = new HashSet<>();
//        Phone phone = new Phone();
//        phone.setNumber("533-202-020");
//        phone.setType(PhoneType.BUSINESS);
//        phone.setUser(ceo);
//        phones.add(phone);
//
//        when(passwordEncoder.encode("admin123")).thenReturn("$2a$10$qSsKGiXLCQnHwuczS/M.HuOaNteaFpDT6rzCAfOXdDIbgu8ta4CFe");
//        when(roleRepository.findByName("ROLE_CEO")).thenReturn(userRole);
//        when(userRepository.save(ceo)).thenReturn(ceo);
//        when(phoneRepository.save(phone)).thenReturn(phone);
//        // 	when
//
//        User result = userService.setupCEO();
//        // 	then
//        assertNotNull(result);
//        assertEquals(result, ceo);
//        assertEquals(ceo.getFirstName(), result.getFirstName());
//        assertEquals(ceo.getPassword(), result.getPassword());
//        assertEquals(ceo.getSalary(), result.getSalary());
//        assertEquals(ceo.getId(), result.getId());
//
//
//    }
//
//    @Test
//    public void findEmployeeTest() {
//        //	given
//        User user = new User();
//        user.setFirstName("d");
//        user.setLastName("k");
//        user.setEmail("i@wp.pl");
//        user.setSalary(new BigDecimal("1000"));
//
//
//        when(userRepository.findOne(1L)).thenReturn(user);
//        // 	when
//        User result = userRepository.findOne(1L);
//        // 	then
//        assertEquals(user.getId(), result.getId());
//        assertEquals(result.getFirstName(), "d");
//    }
//
//    @Test(expected = NotFoundException.class)
//    public void findEmployeeFailTest() {
//        //	given
//        when(userRepository.findOne(3L)).thenThrow(NotFoundException.class);
//        // 	when
//        userService.findEmployee(3L);
//    }
//
//    @Test
//    public void findAllEmployeesOfDepartmentTest() {
//        Department department = new Department();
//        department.setName("it");
//        department.setCity("rzeszow");
//
//        User user = new User();
//        user.setFirstName("d");
//        user.setLastName("k");
//        user.setEmail("i@wp.pl");
//        user.setSalary(new BigDecimal("1000"));
//
//
//        User user2 = new User();
//        user2.setFirstName("d2");
//        user2.setLastName("k2");
//        user2.setEmail("i2@wp.pl");
//        user2.setHead(true);
//        user2.setSalary(new BigDecimal("2000"));
//
//
//        List<User> users = Arrays.asList(user, user2);
//        user.setDepartment(department);
//        user2.setDepartment(department);
//
//
//        when(departmentService.findDepartment(1L)).thenReturn(department);
//        when(userRepository.findAllEmployeesOfDepartment(1L)).thenReturn(users);
//
//
//        //when
//        List<User> allEmployeesOfDepartment = userService.findAllEmployeesOfDepartment(1L);
//        // 	then
//        List<User> result = allEmployeesOfDepartment;
//        assertEquals(allEmployeesOfDepartment.size(), 2);
//        assertEquals(allEmployeesOfDepartment.get(0), user);
//        assertEquals(allEmployeesOfDepartment.get(1), user2);
//
//    }
//
//    @Test
//    public void findAllEmployeesTest() {
//
//        //given
//        User user = new User();
//        user.setFirstName("d");
//        user.setLastName("k");
//        user.setEmail("i@wp.pl");
//        user.setSalary(new BigDecimal("1000"));
//
//
//        User user2 = new User();
//        user2.setFirstName("d2");
//        user2.setLastName("k2");
//        user2.setEmail("i2@wp.pl");
//        user2.setHead(true);
//        user2.setSalary(new BigDecimal("2000"));
//
//        List<User> users = Arrays.asList(user, user2);
//        when(userRepository.findAll()).thenReturn(users);
//        // 	when
//        List<User> result = userService.findAllEmployees();
//        // 	then
//        assertEquals(result.size(), 2);
//        assertEquals(result.get(0), user);
//        assertEquals(result.get(1), user2);
//
//    }
//
//    @Test
//    public void updateEmployeeTest() {
//        //	given
//        User user = new User();
//        user.setFirstName("d");
//        user.setLastName("k");
//        user.setEmail("i@wp.pl");
//        user.setSalary(new BigDecimal("1000"));
//        user.setPassword("$2a$10$qSsKGiXLCQnHwuczS/M.HuOaNteaFpDT6rzCAfOXdDIbgu8ta4CFe");
//        user.setActive(true);
//        Role userRole = roleRepository.findByName("ROLE_HEAD");
//        user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
//        user.setFirstName("test");
//        when(userRepository.findOne(1L)).thenReturn(user);
//        when(passwordEncoder.encode("admin123")).thenReturn("$2a$10$qSsKGiXLCQnHwuczS/M.HuOaNteaFpDT6rzCAfOXdDIbgu8ta4CFe");
////        when(roleRepository.findByName("ROLE_HEAD")).thenReturn(userRole);
////        //when
////        User result = userService.updateEmployee(user);
////        // 	then
////        assertEquals(result, user);
////        assertEquals(result.getFirstName(), user.getFirstName());
////        assertEquals(result.getEmail(), user.getEmail());
//
//    }
//
//    @Test
//    public void createRolesTest() {
//
//        Role role1 = new Role();
//        role1.setName("ROLE_CEO");
//        Role role2 = new Role();
//        role2.setName("ROLE_HEAD");
//        Role role3 = new Role();
//        role3.setName("ROLE_EMPLOYEE");
//
//        when(roleRepository.save(role1)).thenReturn(role1);
//        when(roleRepository.save(role2)).thenReturn(role2);
//        when(roleRepository.save(role3)).thenReturn(role3);
//
//        // 	when
//        userService.createRoles();
//        // 	then
//        verify(roleRepository, times(1)).save(role1);
//        verify(roleRepository, times(1)).save(role2);
//        verify(roleRepository, times(1)).save(role3);
//
//
//    }
//
//    @Test
//    public void createEmployeeTest() {
//        EmployeeDTO employeeDTO = new EmployeeDTO();
//        employeeDTO.setFirstName("damian");
//        employeeDTO.setLastName("krawczyk");
//        employeeDTO.setEmail("irekw@wp.pl");
//        employeeDTO.setSalary(BigDecimal.valueOf(10000));
//        employeeDTO.setPassword("user123");
//
//        User employee = new User();
//        employee.setFirstName(employeeDTO.getFirstName());
//        employee.setLastName(employeeDTO.getLastName());
//        employee.setEmail(employeeDTO.getEmail());
//        employee.setSalary(employeeDTO.getSalary());
//        Role userRole = new Role();
//        Set<Role> roleSet = new HashSet<>();
//        roleSet.add(userRole);
//        userRole.setName("ROLE_EMPLOYEE");
//        employee.setRoles(roleSet);
//        employee.setPassword("$2y$12$xbE5EzPqpC5A5okaKHyRw.j2VtsMmf5cktkjyDTccarDHFlFSQJHm");
//        employee.setActive(true);
//        Set<Phone> phones = new HashSet<>();
//        Phone phone = new Phone();
//        phone.setNumber("533-202-020");
//        phone.setType(PhoneType.BUSINESS);
//        phone.setUser(employee);
//        phones.add(phone);
//
//
//        when(passwordEncoder.encode(employeeDTO.getPassword())).thenReturn("$2y$12$xbE5EzPqpC5A5okaKHyRw.j2VtsMmf5cktkjyDTccarDHFlFSQJHm");
//        when(roleRepository.findByName("ROLE_EMPLOYEE")).thenReturn(userRole);
//        when(userRepository.save(employee)).thenReturn(employee);
//        when(phoneRepository.save(phone)).thenReturn(phone);
//        // 	when
//        User result = userService.createEmployee(employeeDTO);
//        // 	then
//        assertEquals(result, employee);
//        assertEquals(employee.getFirstName(), result.getFirstName());
//
//    }
//
//    @Test
//    public void updatePasswordTest() {
////        User user = new User();
////        user.setFirstName("d");
////        user.setLastName("k");
////        user.setEmail("i@wp.pl");
//////        user.setSalary(new BigDecimal("1000"));
////        user.setPassword("$2a$10$qSsKGiXLCQnHwuczS/M.HuOaNteaFpDT6rzCAfOXdDIbgu8ta4CFe");
//////        user.setActive(true);
//////        Role userRole = roleRepository.findByName("ROLE_HEAD");
//////        user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
//////        user.setFirstName("test");
////        when(userRepository.findOne(1L)).thenReturn(user);
////        when(passwordEncoder.encode("user234")).thenReturn( "$2y$12$JcBkCiqWffy/lPOknWW4T.255VJLvhtztgtYT1fCj6rumtIhHHXHG");
//////        when(roleRepository.findByName("ROLE_HEAD")).thenReturn(userRole);
////        //when
////        User result = userService.updatePassword(user, "user234");
////        // 	then
////        User u = result;
////        assertEquals(result, user);
////        assertEquals(result.getPassword(), "$2y$12$JcBkCiqWffy/lPOknWW4T.255VJLvhtztgtYT1fCj6rumtIhHHXHG");
//
//    }
//
//    @Test
//    public void deleteEmployeeTest() {
//
//        //given
//        Department department = new Department();
//        department.setName("it");
//        department.setCity("rzeszow");
//
//        User user = new User();
//        user.setFirstName("d");
//        user.setLastName("k");
//        user.setEmail("i@wp.pl");
//        user.setSalary(new BigDecimal("1000"));
//
//        List<User> users = Arrays.asList(user);
//        department.setEmployees(users);
//
//        when(departmentService.findDepartment(1L)).thenReturn(department);
//
//
//        Mockito.doNothing().when(userRepository).delete(1L);
//        // 	when
//        userService.deleteEmployee(user);
//        // 	then
//        verify(userRepository, times(1)).delete(user);
//
//    }
//
//    @Test
//    public void deleteEmployeeFailTest() {
//
//        User user = new User();
//        user.setFirstName("d");
//        user.setLastName("k");
//        user.setEmail("i@wp.pl");
//        user.setSalary(new BigDecimal("1000"));
//        Mockito.doNothing().when(userRepository).delete(1L);
//        // 	when
//        userService.deleteEmployee(user);
//        // 	then
//        verify(userRepository, times(1)).delete(user);
//
//    }
//
//    @Test
//    public void findByEmailTest() {
//        User user = new User();
//        user.setFirstName("d");
//        user.setLastName("k");
//        user.setEmail("i@wp.pl");
//        user.setSalary(new BigDecimal("1000"));
//        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
//        // 	when
//        User result = userRepository.findByEmail("i@wp.pl");
//        // 	then
//        assertEquals(user.getId(), result.getId());
//        assertEquals(result.getFirstName(), "d");
//
//    }
}
