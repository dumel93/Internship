package task1.soft.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.Min;
import java.math.BigDecimal;


@Data
public class DepartmentDTO {

    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String city;

    @Min(value = 0)
    @JsonProperty("min_salary")
    private BigDecimal minSalary;

    @Min(value = 0,message = "must be a positive number")
    @JsonProperty("max_salary")
    private BigDecimal maxSalary;

    @JsonProperty("average_salary")
    private BigDecimal averageSalary;

    @JsonProperty("median_salary")
    private BigDecimal medianSalary;

    @JsonProperty("number_of_employees")
    private Integer numberOfEmployees;

    @JsonProperty("head_of_department")
    private EmployeeReadDTO headOfDepartment;

}
