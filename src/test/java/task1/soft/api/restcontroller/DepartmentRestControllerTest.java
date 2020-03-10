package task1.soft.api.restcontroller;


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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Sql(scripts = {"/populate.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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
    public void getDepartmentsSearchTest() throws Exception {


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

        String payload = "{\n" +
                "           \"name\": \"it\",\n" +
                "           \"city\": \"rzeszow\"\n" +
                "      }";


        mockMvc.perform(MockMvcRequestBuilders.post("/departments/")
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.city", is("rzeszow")));

    }

    @Test
    public void updateDepartment() throws Exception {


        String payload = "{\n" +
                "           \"name\": \"it\",\n" +
                "           \"city\": \"wroclaw\"\n" +
                "      }";

        mockMvc.perform(MockMvcRequestBuilders
                .put("/departments/{id}", 2)
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.city", is("wroclaw")));

    }

    @Test
    public void setMinSalaryAndMaxSalary() throws Exception {

        String payload = "{\n" +
                "           \"min_salary\": 1500.0,\n" +
                "           \"max_salary\": 2000.0\n" +
                "        }";
        mockMvc.perform(MockMvcRequestBuilders
                .put("/departments/{id}/salary", 2)
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.min_salary", is(1500.0)))
                .andExpect(jsonPath("$.max_salary", is(2000.0)))
                .andExpect(jsonPath("$.city", is("rzeszow")));


    }

    @Test
    public void deleteDepartment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/departments/{id}", 1))
                .andExpect(status().is2xxSuccessful());
    }
}




