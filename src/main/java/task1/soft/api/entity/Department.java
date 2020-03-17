package task1.soft.api.entity;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "departments")
public class Department {

    @Id
    @Column(name = "department_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    @NotEmpty(message = "name cannot be empty")
    private String name;

    @Column
    @NotEmpty(message = "city cannot be empty")
    private String city;

    @OneToMany(mappedBy = "department", cascade = {CascadeType.ALL})
    private List<User> employees = new ArrayList<>();

    @Column(name = "min_salary")
    @Min(value = 0,message = "must be a positive number")
    private BigDecimal minSalary;

    @Column(name = "max_salary")
    @Min(value = 0,message = "must be a positive number")
    private BigDecimal maxSalary;


}
