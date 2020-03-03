package task1.soft.api.dto;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class DepartmentSalariesDTO {

    @Min(value = 0)
    private Double minSalary;

    @Min(value = 0)
    private Double maxSalary;
}
