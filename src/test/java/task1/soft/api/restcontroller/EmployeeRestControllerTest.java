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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import task1.soft.api.Application;
import task1.soft.api.dto.EmployeeDTO;
import task1.soft.api.dto.EmployeePasswordDTO;
import task1.soft.api.dto.EmployeeSalaryDTO;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts =
        {"classpath:cleanup.sql", "classpath:populate.sql"})
@WithMockUser(username = "ceo@pgs.com", password = "admin123", roles = "CEO")
public class EmployeeRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllEmployees() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].email", is("ceo@pgs.com")))
                .andExpect(jsonPath("$[0].salary", is(20000.0)))
                .andExpect(jsonPath("$[0].first_name", is("admin")))
                .andExpect(jsonPath("$[0].last_name", is("admin")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].email", is("damian@pgs.com")))
                .andExpect(jsonPath("$[1].salary", is(2000.0)))
                .andExpect(jsonPath("$[1].first_name", is("damian")))
                .andExpect(jsonPath("$[1].last_name", is("krawczyk")))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].email", is("adrian@pgs.com")))
                .andExpect(jsonPath("$[2].salary", is(3000.0)))
                .andExpect(jsonPath("$[2].first_name", is("adrian")))
                .andExpect(jsonPath("$[2].last_name", is("strzepek")));

    }


    @Test
    public void testGetEmployeeById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("ceo@pgs.com")))
                .andExpect(jsonPath("$.salary", is(20000.0)))
                .andExpect(jsonPath("$.first_name", is("admin")))
                .andExpect(jsonPath("$.last_name", is("admin")));

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/{id}", 4L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

    }

    @Test
    @WithMockUser(username = "damian@pgs.com", password = "user123", roles = "EMPLOYEE")
    public void testFindAllEmployeesOfDepartment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/employees/departments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].email", is("damian@pgs.com")))
                .andExpect(jsonPath("$[0].salary", is(2000.0)))
                .andExpect(jsonPath("$[0].first_name", is("damian")))
                .andExpect(jsonPath("$[0].last_name", is("krawczyk")))
                .andExpect(jsonPath("$[1].id", is(3)))
                .andExpect(jsonPath("$[1].email", is("adrian@pgs.com")))
                .andExpect(jsonPath("$[1].salary", is(3000.0)))
                .andExpect(jsonPath("$[1].first_name", is("adrian")))
                .andExpect(jsonPath("$[1].last_name", is("strzepek")));

    }

    @Test
    public void testFindEmployeesOfDepartment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/employees/departments/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].email", is("damian@pgs.com")))
                .andExpect(jsonPath("$[0].salary", is(2000.0)))
                .andExpect(jsonPath("$[0].first_name", is("damian")))
                .andExpect(jsonPath("$[0].last_name", is("krawczyk")))
                .andExpect(jsonPath("$[1].id", is(3)))
                .andExpect(jsonPath("$[1].email", is("adrian@pgs.com")))
                .andExpect(jsonPath("$[1].salary", is(3000.0)))
                .andExpect(jsonPath("$[1].first_name", is("adrian")))
                .andExpect(jsonPath("$[1].last_name", is("strzepek")));

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/departments/{id}", 2L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(0)));

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/departments/{id}", 3L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));


    }

    @Test
    public void testCreateEmployee() throws Exception {

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setPassword("user123");
        employeeDTO.setSalary(BigDecimal.valueOf(2000.0));
        employeeDTO.setFirstName("marcin");
        employeeDTO.setLastName("kawalec");
        employeeDTO.setEmail("test@pgs.com");
        employeeDTO.setDepartmentId(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                .content(asJsonString(employeeDTO))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.email", is("test@pgs.com")))
                .andExpect(jsonPath("$.salary", is(2000.0)))
                .andExpect(jsonPath("$.first_name", is("marcin")))
                .andExpect(jsonPath("$.last_name", is("kawalec")));

        EmployeeDTO employeeDTO2 = new EmployeeDTO();
        mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                .content(asJsonString(employeeDTO2))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateEmployeeEmailExists() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setPassword("user123");
        employeeDTO.setSalary(BigDecimal.valueOf(2000.0));
        employeeDTO.setFirstName("damian");
        employeeDTO.setLastName("krawczyk");
        employeeDTO.setEmail("damian@pgs.com");
        employeeDTO.setDepartmentId(1L);
        mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                .content(asJsonString(employeeDTO))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());

    }


    @Test
    public void testUpdateEmployee() throws Exception {

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setPassword("user123");
        employeeDTO.setSalary(BigDecimal.valueOf(2000));
        employeeDTO.setFirstName("test");
        employeeDTO.setLastName("test");
        employeeDTO.setEmail("test2@pgs.com");
        employeeDTO.setDepartmentId(1L);


        mockMvc.perform(MockMvcRequestBuilders.put("/employees/{id}", 2L)
                .content(asJsonString(employeeDTO))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.email", is("test2@pgs.com")))
                .andExpect(jsonPath("$.salary", is(2000)))
                .andExpect(jsonPath("$.first_name", is("test")))
                .andExpect(jsonPath("$.last_name", is("test")));

        EmployeeDTO employeeDTO2 = new EmployeeDTO();
        mockMvc.perform(MockMvcRequestBuilders.put("/employees/{id}", 5L)
                .content(asJsonString(employeeDTO2))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testSetHead() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.put("/employees/departments/head/{id}", 2L)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.email", is("damian@pgs.com")))
                .andExpect(jsonPath("$.salary", is(2000.0)))
                .andExpect(jsonPath("$.first_name", is("damian")))
                .andExpect(jsonPath("$.last_name", is("krawczyk")))
                .andExpect(jsonPath("$.is_head", is(true)));


    }

    @Test
    public void testUpdatePassword() throws Exception {
        EmployeePasswordDTO employeePasswordDTO = new EmployeePasswordDTO();
        employeePasswordDTO.setPassword("test123");
        mockMvc.perform(MockMvcRequestBuilders.put("/employees/{id}/password", 2L)
                .content(asJsonString(employeePasswordDTO))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        employeePasswordDTO.setPassword("test");
        mockMvc.perform(MockMvcRequestBuilders.put("/employees/{id}/password", 2L)
                .content(asJsonString(employeePasswordDTO))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());

        employeePasswordDTO.setPassword("test123");
        mockMvc.perform(MockMvcRequestBuilders.put("/employees/{id}/password", 10L)
                .content(asJsonString(employeePasswordDTO))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());


    }

    @Test
    public void testSetSalary() throws Exception {
        EmployeeSalaryDTO employeeSalaryDTO = new EmployeeSalaryDTO();
        employeeSalaryDTO.setSalary(BigDecimal.valueOf(1));
        mockMvc.perform(MockMvcRequestBuilders.put("/employees/{id}/salary", 2L)
                .content(asJsonString(employeeSalaryDTO))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.email", is("damian@pgs.com")))
                .andExpect(jsonPath("$.salary", is(1000.0)))
                .andExpect(jsonPath("$.first_name", is("damian")))
                .andExpect(jsonPath("$.last_name", is("krawczyk")));


        employeeSalaryDTO.setSalary(BigDecimal.valueOf(1000000));
        mockMvc.perform(MockMvcRequestBuilders.put("/employees/{id}/salary", 2L)
                .content(asJsonString(employeeSalaryDTO))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.email", is("damian@pgs.com")))
                .andExpect(jsonPath("$.salary", is(5000.0)))
                .andExpect(jsonPath("$.first_name", is("damian")))
                .andExpect(jsonPath("$.last_name", is("krawczyk")));

        employeeSalaryDTO.setSalary(BigDecimal.valueOf(3000.0));
        mockMvc.perform(MockMvcRequestBuilders.put("/employees/{id}/salary", 2L)
                .content(asJsonString(employeeSalaryDTO))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.email", is("damian@pgs.com")))
                .andExpect(jsonPath("$.salary", is(3000.0)))
                .andExpect(jsonPath("$.first_name", is("damian")))
                .andExpect(jsonPath("$.last_name", is("krawczyk")));

        employeeSalaryDTO.setSalary(BigDecimal.valueOf(3000.0));
        mockMvc.perform(MockMvcRequestBuilders.put("/employees/{id}/salary", 10L)
                .content(asJsonString(employeeSalaryDTO))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDisable() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.put("/employees/{id}/active", 2L)

                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_active", is(false)));


        mockMvc.perform(MockMvcRequestBuilders.put("/employees/{id}/active", 10L)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/employees/{id}", 2L))
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
