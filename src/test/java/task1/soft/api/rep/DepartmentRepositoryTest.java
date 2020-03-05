package task1.soft.api.rep;


import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import task1.soft.api.Application;
import task1.soft.api.entity.Department;
import task1.soft.api.repo.DepartmentRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes={Application.class})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DepartmentRepositoryTest {


    private TestEntityManager entityManager;
    private DepartmentRepository departmentRepository;


    @Test
    public void saveTest() {
        Department department = new Department();
        department.setName("it");
        department.setId(1L);
        department.setCity("rzeszow");
        entityManager.persist(department);
        Assert.assertNotNull(departmentRepository.findOne(1L));
    }


}
