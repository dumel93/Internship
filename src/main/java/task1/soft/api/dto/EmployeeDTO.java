package task1.soft.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import task1.soft.api.service.UserService;
import task1.soft.api.validation.Unique;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class EmployeeDTO {

    private Long id;

    @NotEmpty
    @JsonProperty("first_name")
    private String firstName;

    @NotEmpty
    @JsonProperty("last_name")
    private String lastName;

    private List<PhoneDTO> phones;

    @NotEmpty
    @Email
    @Unique(service = UserService.class, fieldName = "email")
    private String email;

    @NotEmpty
    @Length(min = 6)
    @JsonProperty("password")
    private String password;

    @Min(value = 0)
    @NotNull
    private BigDecimal salary;

    private boolean isHead;

    private boolean isActive;

    @NotNull
    @JsonProperty("department_id")
    private Long departmentId;

}
