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
import task1.soft.api.repo.RoleRepository;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Sql("/data_schema.sql")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@WithMockUser(username = "ceo@pgs.com", password = "admin123", roles = "CEO")
public class DepartmentRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleRepository roleRepository;

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
    public void getDepartmentsSearchTest() throws Exception {


        mockMvc.perform(MockMvcRequestBuilders.get("/departments?search=name:hr"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].city", is("rzeszow")))
                .andExpect(jsonPath("$[0].name", is("hr")));

        mockMvc.perform(MockMvcRequestBuilders.get("/departments?search=city:rzeszow"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].city", is("rzeszow")))
                .andExpect(jsonPath("$[0].name", is("it")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].city", is("rzeszow")))
                .andExpect(jsonPath("$[1].name", is("hr")));

        mockMvc.perform(MockMvcRequestBuilders.get("/departments?sortBy=name&orderBy=asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].id", is(1)))
                .andExpect(jsonPath("$[1].city", is("rzeszow")))
                .andExpect(jsonPath("$[1].name", is("it")))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].city", is("rzeszow")))
                .andExpect(jsonPath("$[0].name", is("hr")));

    }

    @Test
    public void getDepartmentTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/departments/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.city", is("rzeszow")))
                .andExpect(jsonPath("$.name", is("it")));
    }
    
    @Test
    public void createDepartment() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/departments/")
                .content(asJsonString(new DepartmentDTO("pgs", "wroclaw")))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.city", is("wroclaw")));

    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void updateDepartment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .put("/departments/{id}", 2)
                .content(asJsonString(new DepartmentDTO("hr", "wroclaw")))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.city", is("wroclaw")));

    }

    @Test
    public void setMinSalaryAndMaxSalary() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .put("/departments/{id}/salary", 2)
                .content(asJsonString(new DepartmentSalariesDTO(new BigDecimal("2000"), new BigDecimal("3000"))))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.min_salary", is(2000)))
                .andExpect(jsonPath("$.max_salary", is(3000)))
                .andExpect(jsonPath("$.city", is("rzeszow")));


    }

    @Test
    public void deleteDepartment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/departments/{id}", 1))
                .andExpect(status().is2xxSuccessful());
    }
}




