package task1.soft.api.dto;

import lombok.Data;
import task1.soft.api.entity.Department;
import task1.soft.api.util.DTOEntity;

import javax.validation.constraints.NotNull;

@Data
public class EmployeeDTO implements DTOEntity {


    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String email;

    @NotNull
    private String password;

    private Long departmentId;

}
