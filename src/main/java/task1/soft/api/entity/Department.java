package task1.soft.api.entity;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
    @NotNull
    private String name;

    @Column
    @NotNull
    private String city;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "department", cascade = CascadeType.ALL)
    private List<User> employees=new ArrayList<>();;

    @Column
    @Min(value = 0)
    private BigDecimal minSalary;

    @Column
    @Min(value = 0)
    private BigDecimal maxSalary;

}