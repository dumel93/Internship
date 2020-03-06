package task1.soft.api.repo;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import task1.soft.api.entity.Department;
import task1.soft.api.entity.User;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class DepartmentRepositoryTest {

    @Autowired
    private  TestEntityManager entityManager;
    @Autowired
    private  DepartmentRepository departmentRepository;

    @Test
    public void countAverageSalariesTest() {
        Department department = new Department();
        department.setName("it");
        department.setCity("rzeszow");

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
        user2.setSalary(new BigDecimal("2000"));
        user2.setDepartment(department);


        entityManager.persist(department);
        entityManager.persist(user);
        entityManager.persist(user2);
        Assert.assertEquals(new BigDecimal("1500.0"), departmentRepository.countAverageSalaries(department.getId()));
    }


    @Test
    public void findHeadByIdDepartTest() {
        Department department2 = new Department();
        department2.setName("it");
        department2.setCity("rzeszow");

        User user3 = new User();
        user3.setFirstName("d");
        user3.setLastName("k");
        user3.setEmail("i3@wp.pl");
        user3.setSalary(new BigDecimal("1000"));
        user3.setDepartment(department2);

        User user4 = new User();
        user4.setFirstName("d2");
        user4.setLastName("k2");
        user4.setEmail("i4@wp.pl");
        user4.setHead(true);
        user4.setSalary(new BigDecimal("2000"));
        user4.setDepartment(department2);


        entityManager.persist(department2);
        entityManager.persist(user3);
        entityManager.persist(user4);
        Assert.assertEquals(user4, departmentRepository.findHeadByIdDepart(department2.getId()));
    }

    @Test
    public void countEmployeesByDepartIdTest() {
        Department department3 = new Department();
        department3.setName("it");
        department3.setCity("rzeszow");

        User user5 = new User();
        user5.setFirstName("d");
        user5.setLastName("k");
        user5.setEmail("i5@wp.pl");
        user5.setSalary(new BigDecimal("1000"));
        user5.setDepartment(department3);

        User user6 = new User();
        user6.setFirstName("d2");
        user6.setLastName("k2");
        user6.setEmail("i6@wp.pl");
        user6.setSalary(new BigDecimal("2000"));
        user6.setDepartment(department3);


        entityManager.persist(department3);
        entityManager.persist(user5);
        entityManager.persist(user6);
        Assert.assertEquals(2, departmentRepository.countEmployeesByDepartId(department3.getId()));
    }


}
