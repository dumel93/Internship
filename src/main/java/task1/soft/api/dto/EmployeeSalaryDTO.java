package task1.soft.api.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class EmployeeSalaryDTO {

    @Min(value = 0)
    @NotNull
    private BigDecimal salary;
}
