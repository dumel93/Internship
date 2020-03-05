package task1.soft.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import java.math.BigDecimal;



@Data
public class DepartmentDTO {

    @Id
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String city;

    @Min(value = 0)
    @JsonProperty("min_salary")
    private BigDecimal minSalary;

    @Min(value = 0)
    @JsonProperty("max_salary")
    private BigDecimal maxSalary;

    private BigDecimal averageSalary;
    private BigDecimal medianSalary;
    private Integer numberOfEmployees;

    @JsonProperty("head_of_department")
    private EmployeeReadDTO headOfDepartment;
}
