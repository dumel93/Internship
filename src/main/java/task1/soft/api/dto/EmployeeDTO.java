package task1.soft.api.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import task1.soft.api.entity.Phone;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;


@Data
public class EmployeeDTO {

    private Long id;

    @NotNull
    @JsonProperty("first_name")
    private String firstName;

    @NotNull
    @JsonProperty("last_name")
    private String lastName;

    private boolean active;

    private Set<Phone> phones;

    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    @Length(min = 6)
    @JsonProperty("password")
    private String password;

    @Min(value = 0)
    @NotNull
    private BigDecimal salary;

    private boolean isHead;

    @JsonProperty("department_id")
    private Long departmentId;


    @JsonProperty("created_date")
    private LocalDate dateOfEmployment=LocalDate.now();

    @JsonProperty("last_login_time")
    private LocalDateTime lastLoginTime;


}
