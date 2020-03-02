package task1.soft.api.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class EmployeeDTO {

    private Long employeeId;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String email;

    private DepartmentDTO department;

}
