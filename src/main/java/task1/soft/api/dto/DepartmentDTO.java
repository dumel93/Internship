package task1.soft.api.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Data
public class DepartmentDTO {

    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String city;

    private Integer numberOfEmployees;
    private BigDecimal averageSalary;
    private BigDecimal medianSalary;
    private List<EmployeeDTO> employees=new ArrayList<>();
}
