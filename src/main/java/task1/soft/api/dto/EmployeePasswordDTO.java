package task1.soft.api.dto;

import lombok.Data;
import task1.soft.api.util.DTOEntity;

@Data
public class EmployeePasswordDTO implements DTOEntity {

    private String password;
}
