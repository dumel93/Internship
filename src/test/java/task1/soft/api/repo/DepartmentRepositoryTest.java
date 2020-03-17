package task1.soft.api.repo;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import task1.soft.api.entity.User;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts =
        {"classpath:cleanup.sql", "classpath:populate.sql"})
public class DepartmentRepositoryTest {


    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    public void testCountAverageSalaries() {

        Assert.assertEquals(new BigDecimal("2500.0"), departmentRepository.countAverageSalaries(1L));
    }

    @Test
    public void testFindHeadByIdDepart() {
        User head= departmentRepository.findHeadByIdDepart(1L);
        Assert.assertEquals(head, departmentRepository.findHeadByIdDepart(1L));
    }

    @Test
    public void testCountEmployeesByDepartId() {

        Assert.assertEquals(2, departmentRepository.countEmployeesByIdDepart(1L));
        Assert.assertEquals(0, departmentRepository.countEmployeesByIdDepart(2L));
    }
}
