package task1.soft.api.dto;

import lombok.Data;
import task1.soft.api.util.DTOEntity;


@Data
public class DepartmentDTO implements DTOEntity {

    private String name;

    private String city;

}
