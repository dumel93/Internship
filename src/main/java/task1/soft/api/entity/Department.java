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

    @OneToMany(mappedBy = "department",cascade = {CascadeType.ALL})
    private List<User> employees = new ArrayList<>();

    @Column
    @Min(value = 0)
    private BigDecimal minSalary;

    @Column
    @Min(value = 0)
    private BigDecimal maxSalary;



    /**
     * Add new employee to the department. The method keeps
     * relationships consistency:
     * * this department is set as the employee owner
     */
    public void addEmployees(User employee) {

        //prevent endless loop
        if (employees.contains(employee))
            return;
        //add new employee
        employees.add(employee);
        //set myself into the employee account
        employee.setDepartment(this);

    }

    //defensive copy, nobody will be able to change
    //the list from the outside
    public List<User> getEmployees() {
        return employees;
    }


    /**
     * Removes the employee from the department. The method keeps
     * relationships consistency:
     */

    public void removeEmployee(User employee) {

        //prevent endless loop
        if (!employees.contains(employee))
            return;
        //remove the account
        employees.remove(employee);
        //remove myself from the twitter account
        employee.setDepartment(null);

    }

}