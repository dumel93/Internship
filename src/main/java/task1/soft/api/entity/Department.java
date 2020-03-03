package task1.soft.api.entity;

import lombok.Data;
import javax.persistence.*;
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
    private String name;

    @Column
    private String city;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "department", cascade = CascadeType.ALL)
    private List<User> employees;

    @Column
    private Integer numberOfEmployees;

    @Column
    private Double minSalary = 0d;

    @Column
    private Double maxSalary = 0d;

    @Column
    private Double averageSalary=0d;

    @Column
    private Double medianSalary=0d;

    public Department() {
        employees = new ArrayList<>();
    }

}