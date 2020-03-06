package task1.soft.api.restcontroller;


import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import task1.soft.api.Application;
import task1.soft.api.entity.Department;
import task1.soft.api.repo.DepartmentRepository;
import task1.soft.api.repo.UserRepository;
import task1.soft.api.service.DepartmentService;
import task1.soft.api.service.DepartmentServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
public class DepartmentRestControllerTest {


    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private TestEntityManager testEntityManager;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    @WithMockUser(username = "ceo@pgs.com",password = "admin123",roles = "CEO")
    public void getDepartmentsTest() throws Exception {
        Department department1 = new Department();
        department1.setName("it");
        department1.setCity("rzeszow");


        Department department2 = new Department();
        department2.setName("hr");
        department2.setCity("rzeszow");

        testEntityManager.persist(department1);
        testEntityManager.persist(department2);


        mockMvc.perform(MockMvcRequestBuilders.get("/departments"))
//                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].city", is("rzeszow")))
                .andExpect(jsonPath("$[0].name", is("it")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].description", is("rzeszow")))
                .andExpect(jsonPath("$[1].title", is("hr")));
    }
}




