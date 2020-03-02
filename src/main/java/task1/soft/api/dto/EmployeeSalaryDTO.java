package task1.soft.api.dto;

import lombok.Data;
import task1.soft.api.util.DTOEntity;

@Data
public class EmployeeSalaryDTO implements DTOEntity {

    private Double salary;
}
