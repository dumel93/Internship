package task1.soft.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Data
public class DepartmentSalariesDTO {

    @Min(value = 0)
    @JsonProperty("min_salary")
    private BigDecimal minSalary;

    @Min(value = 0)
    @JsonProperty("max_salary")
    private BigDecimal maxSalary;

}
