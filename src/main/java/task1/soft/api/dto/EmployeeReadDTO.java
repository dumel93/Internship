package task1.soft.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EmployeeReadDTO {

    private Long id;

    @NotNull
    @JsonProperty("first_name")
    private String firstName;

    @NotNull
    @JsonProperty("last_name")
    private String lastName;

    @NotNull
    @Email
    private String email;


    @Min(value = 0)
    @NotNull
    private BigDecimal salary;

    @JsonProperty("name_of_the_department")
    private String nameOfDepartment;

    @JsonProperty("department_id")
    @JsonIgnore
    private Long departmentId;

    @JsonProperty("is_active")
    private boolean isActive;
    @JsonProperty("is_head")
    private boolean isHead;

    @JsonProperty("created_date")
    private LocalDate dateOfEmployment;

    @JsonProperty("last_login_time")
    private LocalDateTime lastLoginTime;

    private List<PhoneDTO> phones;

    @ToString.Exclude
    @JsonProperty("department_head_profile")
    private EmployeeReadDTO departmentHeadProfile;

}
