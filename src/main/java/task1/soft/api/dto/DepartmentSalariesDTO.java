package task1.soft.api.dto;

import lombok.Data;
import task1.soft.api.util.DTOEntity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class DepartmentSalariesDTO  implements DTOEntity {


    @NotNull
    @Min(value = 0)
    private Double minSalary;

    @NotNull
    @Min(value = 0)
    private Double maxSalary;
}
