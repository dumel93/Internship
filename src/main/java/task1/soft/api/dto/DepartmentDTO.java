package task1.soft.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import task1.soft.api.entity.User;

import javax.persistence.Id;
import java.math.BigDecimal;



@Data
public class DepartmentDTO {

    @Id
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String city;


    private Integer numberOfEmployees;
    private BigDecimal averageSalary;
    private BigDecimal medianSalary;

    @JsonProperty("head_of_department")
    private EmployeeDTO headOfDepartment;
}
