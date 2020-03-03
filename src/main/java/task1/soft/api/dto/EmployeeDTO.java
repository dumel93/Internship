package task1.soft.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
public class EmployeeDTO {

    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Length(min = 6)
    @JsonIgnore
    private String password;

    @Min(value = 0)
    @NotNull
    private BigDecimal salary;

    private Long departmentId;

    private boolean isHead;

    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    @Column(name = "last_login_time")
    private LocalDate dateOfEmployment;

    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;


}
