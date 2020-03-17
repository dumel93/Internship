package task1.soft.api.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import task1.soft.api.dto.DepartmentDTO;
import task1.soft.api.dto.EmployeeReadDTO;
import task1.soft.api.entity.Department;
import task1.soft.api.entity.User;
import task1.soft.api.exception.NoDeletePermissionException;
import task1.soft.api.exception.NotFoundException;
import task1.soft.api.repo.DepartmentRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


//BDD
@RunWith(MockitoJUnitRunner.class)
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DepartmentService departmentService = new DepartmentServiceImpl(departmentRepository, modelMapper);

    @Test
    public void testCreateDepartment() {
        //	given
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName("it");
        departmentDTO.setCity("rzeszow");

        Department department = new Department();
        department.setName(departmentDTO.getName());
        department.setCity(departmentDTO.getCity());

        when(modelMapper.map(departmentDTO, Department.class)).thenReturn(department);
        when(departmentRepository.save(department)).thenReturn(department);

        // 	when
        Department result = departmentService.createDepartment(departmentDTO);
        // 	then
        assertEquals(department.getName(), result.getName());

    }

    @Test
    public void testUpdateDepartment() {
        //	given
        Department department = new Department();
        department.setName("it");
        department.setCity("rzeszow");

        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName(department.getName());
        departmentDTO.setCity(department.getCity());

        // 	when
        when(departmentRepository.findOne(1L)).thenReturn(department);
        when(modelMapper.map(departmentDTO, Department.class)).thenReturn(department);
        when(departmentRepository.save(department)).thenReturn(department);

        departmentDTO.setName("test");
        department.setName(departmentDTO.getName());

        Department result = departmentService.updateDepartment(departmentDTO);
        // 	then
        assertEquals(department.getId(), result.getId());
        assertEquals(result.getName(), department.getName());

    }

    @Test(expected = NotFoundException.class)
    public void testUpdateDepartmentFail() throws Throwable {
        //	given
        Department department = new Department();
        department.setName("it");
        department.setCity("rzeszow");

        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName(department.getName());
        departmentDTO.setCity(department.getCity());

        when(departmentRepository.findOne(3L)).thenThrow(new NotFoundException("There is no Department with id: " + 3));
        when(modelMapper.map(departmentDTO, Department.class)).thenReturn(department);

        // 	when
        departmentDTO.setName("test");
        departmentService.updateDepartment(departmentDTO);
        assertTrue(throwException());

    }

    private boolean throwException() {
        throw new NotFoundException("There is no Department with id: " + 3);
    }

    @Test
    public void testFindOneDepartment() {
        //	given
        Department department = new Department();
        department.setName("it");
        department.setCity("rzeszow");

        when(departmentRepository.findOne(1L)).thenReturn(department);

        // 	when
        Department result = departmentService.findDepartment(1L);
        // 	then
        assertEquals(department.getId(), result.getId());
        assertEquals(result.getName(), "it");
    }

    @Test(expected = NotFoundException.class)
    public void testFindOneDepartmentFail() {
        //	given
        when(departmentRepository.findOne(3L)).thenThrow(new NotFoundException("There is no Department with id: " + 3));
        // 	when
        departmentService.findDepartment(3L);
    }

    @Test
    public void testDeleteDepartmentWithoutEmployees() {
        //given
        Department department = new Department();
        department.setName("it");
        department.setCity("rzeszow");
        department.setEmployees(new ArrayList<>());
        when(departmentRepository.findOne(1L)).thenReturn(department);
        Mockito.doNothing().when(departmentRepository).delete(1L);
        // 	when
        departmentService.deleteDepartment(1L);
        // 	then
        verify(departmentRepository, times(1)).delete(1L);


    }

    @Test(expected = NoDeletePermissionException.class)
    public void testDeleteDepartmentWithEmployees() {
        Department department = new Department();
        department.setName("it");
        department.setCity("rzeszow");
        User user = new User();
        user.setFirstName("d");
        user.setLastName("k");
        user.setEmail("i@wp.pl");
        user.setSalary(new BigDecimal("1000"));
        user.setDepartment(department);

        when(departmentRepository.findOne(1L)).thenReturn(department);
        doThrow(new NoDeletePermissionException("Cannot delete this department because there are still employees"))
                .when(departmentRepository).delete(1L);

        departmentService.deleteDepartment(1L);

        verify(departmentRepository, times(0)).delete(1L);
    }


    @Test
    public void testGetAllDepartmentDetails() {
        //given
        Department department = new Department();
        department.setName("it");
        department.setCity("rzeszow");
        department.setId(1L);

        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName(department.getName());
        departmentDTO.setCity(department.getCity());
        departmentDTO.setId(department.getId());

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


        when(departmentRepository.findOne(1L)).thenReturn(department);
        when(departmentRepository.countEmployeesByIdDepart(1L)).thenReturn(2);
        when(departmentRepository.countAverageSalaries(1L)).thenReturn(new BigDecimal(("1500")));
        when(departmentRepository.findHeadByIdDepart(1L)).thenReturn(user2);

        EmployeeReadDTO employeeReadDTO = new EmployeeReadDTO();
        employeeReadDTO.setFirstName("d2");
        employeeReadDTO.setLastName("k2");
        employeeReadDTO.setEmail("i2@wp.pl");
        employeeReadDTO.setSalary(new BigDecimal("2000"));
        employeeReadDTO.setNameOfDepartment("it");
        employeeReadDTO.setId(1L);

        when(modelMapper.map(user2, EmployeeReadDTO.class)).thenReturn(employeeReadDTO);
        when(modelMapper.map(department, DepartmentDTO.class)).thenReturn(departmentDTO);
        //when
        DepartmentDTO result = departmentService.getAllDepartmentDetails(1L);
        // 	then
        assertEquals(result.getNumberOfEmployees(), Integer.valueOf(2));
        assertEquals(result.getAverageSalary(), new BigDecimal("1500"));
        assertEquals(user2.getFirstName(), result.getHeadOfDepartment().getFirstName());
        assertEquals(user2.getFirstName(), result.getHeadOfDepartment().getFirstName());

    }

    @Test
    public void testCalculateMedian() {
        //given
        Department department = new Department();
        department.setName("it");
        department.setCity("rzeszow");

        User user1 = new User();
        user1.setFirstName("d");
        user1.setSalary(new BigDecimal("1000"));

        User user2 = new User();
        user2.setFirstName("d2");
        user2.setSalary(new BigDecimal("2000"));

        User user3 = new User();
        user3.setFirstName("d2");
        user3.setSalary(new BigDecimal("3000"));

        department.getEmployees().add(user1);
        department.getEmployees().add(user2);
        department.getEmployees().add(user3);

        when(departmentRepository.findOne(1L)).thenReturn(department);

        //when
        BigDecimal result = departmentService.calculateMedian(1L);
        //then
        assertEquals(new BigDecimal("2000"), result);
    }

    @Test
    public void testFindAllDepartments() {
        //given
        Department department = new Department();
        department.setName("it");
        department.setCity("rzeszow");

        Department department2 = new Department();
        department2.setName("it2");
        department2.setCity("rzeszow2");
        List<Department> list = new ArrayList<>();

        list.add(department);
        list.add(department2);
        when(departmentRepository.findAll()).thenReturn(list);
        // 	when
        List<Department> result = departmentService.findAllDepartments();
        // 	then
        assertEquals(result.size(), 2);
        assertEquals(result.get(1), department2);
    }

}

