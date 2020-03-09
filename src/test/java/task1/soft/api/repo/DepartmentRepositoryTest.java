package task1.soft.api.repo;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import task1.soft.api.entity.Department;
import task1.soft.api.entity.User;
import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DataJpaTest
@Sql("/data_schema.sql")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class DepartmentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    public void countAverageSalariesTest() {

        Assert.assertEquals(new BigDecimal("2500.0"), departmentRepository.countAverageSalaries(1L));
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

        Assert.assertEquals(2, departmentRepository.countEmployeesByDepartId(1L));

    }

}
