package task1.soft.api.entity;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "departments")
public class Departament {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "department_id")
    private Long id;

    @Column
    private String name;

    @Column
    private String city;



    @OneToOne(targetEntity=User.class,cascade=CascadeType.ALL)
    private User headOfDepartament;


    @Column
    private Integer numberOfEmployyes;

    @Column
    private Float minSalary;

    @Column
    private Float maxSalary;

    @Column
    private Float averageSalary;

    @Column
    private Float medianSalary;





}
