package task1.soft.api.restcontroller;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import task1.soft.api.entity.Department;
import task1.soft.api.repo.DepartmentRepository;
import task1.soft.api.repo.UserRepository;
import task1.soft.api.service.DepartmentService;
import static org.hamcrest.Matchers.*;
import task1.soft.api.service.DepartmentServiceImpl;
import java.util.Arrays;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
@SpringBootTest
public class DepartmentRestControllerTest {


    @Autowired
    private WebApplicationContext webApplicationContext;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Autowired
    private TestEntityManager entityManager;

    @Mock
    private DepartmentService departmentService=  new DepartmentServiceImpl(userRepository, departmentRepository, modelMapper);

    private MockMvc mockMvc;


    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getDepartmentsTest() throws Exception {

        assertThat(departmentService).isNotNull();
        Department department1 = new Department();
        department1.setName("it");
        department1.setCity("rzeszow");


        Department department2 = new Department();
        department2.setName("hr");
        department2.setCity("rzeszow");

        entityManager.persist(department1);
        entityManager.persist(department2);

        when(departmentService.findAll()).thenReturn(Arrays.asList(department1, department2));
        mockMvc.perform(get("/departments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].city", is("rzeszow")))
                .andExpect(jsonPath("$[0].name", is("it")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].description", is("rzeszow")))
                .andExpect(jsonPath("$[1].title", is("hr")));

        verify(departmentService, times(1)).findAll();
        verifyNoMoreInteractions(departmentService);


    }
}




