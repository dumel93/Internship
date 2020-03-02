package task1.soft.api.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import task1.soft.api.util.DTOEntity;
import javax.validation.constraints.NotNull;

@Data
public class DepartmentDTO implements DTOEntity {

    private Long id;

    @NotEmpty
    @NotNull
    private String name;

    @NotNull
    @NotEmpty
    private String city;

}
