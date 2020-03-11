package task1.soft.api.restcontroller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import task1.soft.api.Application;
import task1.soft.api.dto.DepartmentDTO;
import task1.soft.api.dto.DepartmentSalariesDTO;
import java.math.BigDecimal;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts =
        {"classpath:cleanup.sql", "classpath:populate.sql"})
@WithMockUser(username = "ceo@pgs.com", password = "admin123", roles = "CEO")
public class DepartmentRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getDepartmentsTest() throws Exception {


        mockMvc.perform(MockMvcRequestBuilders.get("/departments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].city", is("rzeszow")))
                .andExpect(jsonPath("$[0].name", is("it")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].city", is("rzeszow")))
                .andExpect(jsonPath("$[1].name", is("hr")));

    }


    @Test
    public void getDepartmentTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/departments/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.city", is("rzeszow")))
                .andExpect(jsonPath("$.name", is("it")));

        mockMvc.perform(MockMvcRequestBuilders.get("/departments/{id}", 3L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

    }


    @Test
    public void createDepartmentTest() throws Exception {

        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName("it");
        departmentDTO.setCity("rzeszow");

        mockMvc.perform(MockMvcRequestBuilders.post("/departments/")
                .content(asJsonString(departmentDTO))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.city", is("rzeszow")));

    }

    @Test
    public void createDepartmentFailTest() throws Exception {
        DepartmentDTO departmentDTO2 = new DepartmentDTO();
        departmentDTO2.setName("");
        departmentDTO2.setCity("");

        mockMvc.perform(MockMvcRequestBuilders.post("/departments/")
                .content(asJsonString(departmentDTO2))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());

        departmentDTO2.setName("it");
        departmentDTO2.setCity("rzeszow");
        departmentDTO2.setMinSalary(BigDecimal.valueOf(-200));
        mockMvc.perform(MockMvcRequestBuilders.post("/departments/")
                .content(asJsonString(departmentDTO2))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void updateDepartmentTest() throws Exception {


        DepartmentDTO departmentDTO= new DepartmentDTO();
        departmentDTO.setName("it");
        departmentDTO.setCity("wroclaw");

        mockMvc.perform(MockMvcRequestBuilders
                .put("/departments/{id}", 2)
                .content(asJsonString(departmentDTO))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.city", is("wroclaw")));

    }

    @Test
    public void updateDepartmentFailTest() throws Exception {


        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName("it");
        departmentDTO.setCity("wroclaw");

        mockMvc.perform(MockMvcRequestBuilders
                .put("/departments/{id}", 4)
                .content(asJsonString(departmentDTO))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

    }

    @Test
    public void setMinSalaryAndMaxSalaryTest() throws Exception {

        DepartmentSalariesDTO departmentSalariesDTO = new DepartmentSalariesDTO();
        departmentSalariesDTO.setMinSalary(BigDecimal.valueOf(2000));
        departmentSalariesDTO.setMaxSalary(BigDecimal.valueOf(3000));
        mockMvc.perform(MockMvcRequestBuilders
                .put("/departments/{id}/salary", 2)
                .content(asJsonString(departmentSalariesDTO))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.min_salary", is(2000)))
                .andExpect(jsonPath("$.max_salary", is(3000)))
                .andExpect(jsonPath("$.city", is("rzeszow")));

    }

    @Test
    public void setMinSalaryAndMaxSalaryFailTest() throws Exception {

        DepartmentSalariesDTO departmentSalariesDTO = new DepartmentSalariesDTO();
        departmentSalariesDTO.setMinSalary(BigDecimal.valueOf(-2000));
        departmentSalariesDTO.setMaxSalary(BigDecimal.valueOf(-3000));
        mockMvc.perform(MockMvcRequestBuilders
                .put("/departments/{id}/salary", 2)
                .content(asJsonString(departmentSalariesDTO))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void deleteDepartmentWithNoPermissionTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/departments/{id}", 1))
                .andExpect(status().isConflict());
    }

    @Test
    public void deleteDepartmentWithPermissionTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/departments/{id}", 2))
                .andExpect(status().isNoContent());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}




