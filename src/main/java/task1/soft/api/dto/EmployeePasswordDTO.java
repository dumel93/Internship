package task1.soft.api.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class EmployeePasswordDTO {

    @NotNull
    @Length(min = 6)
    @Email
    private String password;
}
