package task1.soft.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import task1.soft.api.entity.Department;


@Data
public class EmployeeDTO {

    private Long employeeId;

    private String firstName;

    private String lastName;

    private String email;

    private DepartmentDTO department;

}
