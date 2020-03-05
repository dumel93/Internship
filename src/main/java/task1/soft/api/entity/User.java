package task1.soft.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;


import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column
    @NotNull
    private String firstName;

    @NotNull
    @Column
    private String lastName;

    @Email
    @NotNull
    @Column(unique = true)
    private String email;

    @Column
    @Length(min = 6, message = "*Your password must have at least 5 characters")
    private String password;

    @Column(name = "active")
    private boolean active;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;


    @ManyToOne
    private Department department;

    @JsonIgnore
    @Column
    private boolean isHead;

    @Column
    @Min(value = 0)
    private BigDecimal salary;

    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    @Column(name = "created_date",columnDefinition = "DATE")
    private LocalDate dateOfEmployment=LocalDate.now();

    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;


}




