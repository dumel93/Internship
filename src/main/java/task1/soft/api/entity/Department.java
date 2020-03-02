package task1.soft.api.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private Double averageSalary;

    @Column
    private Double medianSalary;

    public Department() {
        employees = new ArrayList<>();
    }



}