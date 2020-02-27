package task1.soft.api.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "departments")
public class Departament {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column
    private String city;


    @Column
    private Long headOfDepartmentUserId;

    @OneToMany(mappedBy	="departament",cascade=CascadeType.ALL)
    private List<User> employyers=new ArrayList<>();





    public List<User> getEmployyers() {
        return employyers;
    }

    public void setEmployyers(List<User> employyers) {
        this.employyers = employyers;
    }

    @Column
    private Integer numberOfEmployyes;

    @Column
    private Float minSalary=0f;

    @Column
    private Float maxSalary=0f;

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

    public void setHeadOfDepartament(Long userId) {
    }


    public Long getHeadOfDepartmentUserId() {
        return headOfDepartmentUserId;
    }

    public void setHeadOfDepartmentUserId(Long headOfDepartmentUserId) {
        this.headOfDepartmentUserId = headOfDepartmentUserId;
}}
