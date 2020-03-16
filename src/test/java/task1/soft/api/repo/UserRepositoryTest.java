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
import task1.soft.api.entity.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts =
        {"classpath:cleanup.sql", "classpath:populate.sql"})
public class UserRepositoryTest {
    

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindByEmail() {

        User user = userRepository.findOne(1L);
        Assert.assertEquals(user, userRepository.findByEmail("ceo@pgs.com"));
    }

    @Test
    public void testFindAllEmployeesOfDepartment() {
        User user = userRepository.findOne(2L);
        User user2 = userRepository.findOne(3L);
        List<User> users = Arrays.asList(user, user2);
        Assert.assertEquals(users, userRepository.findAllEmployeesOfDepartment(1L));
    }

    @Test
    public void testSetLoginTime() {

        userRepository.setLoginTime(2L);
        User user = userRepository.findOne(2L);
        LocalDateTime lastLoginTime = user.getLastLoginTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = lastLoginTime.format(formatter);
        Assert.assertEquals(formatDateTime, LocalDateTime.now().format(formatter));
    }
}
