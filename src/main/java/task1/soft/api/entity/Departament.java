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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public User getHeadOfDepartament() {
        return headOfDepartament;
    }

    public void setHeadOfDepartament(User headOfDepartament) {
        this.headOfDepartament = headOfDepartament;
    }

    public Integer getNumberOfEmployyes() {
        return numberOfEmployyes;
    }

    public void setNumberOfEmployyes(Integer numberOfEmployyes) {
        this.numberOfEmployyes = numberOfEmployyes;
    }

    public Float getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(Float minSalary) {
        this.minSalary = minSalary;
    }

    public Float getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(Float maxSalary) {
        this.maxSalary = maxSalary;
    }

    public Float getAverageSalary() {
        return averageSalary;
    }

    public void setAverageSalary(Float averageSalary) {
        this.averageSalary = averageSalary;
    }

    public Float getMedianSalary() {
        return medianSalary;
    }

    public void setMedianSalary(Float medianSalary) {
        this.medianSalary = medianSalary;
    }
}
