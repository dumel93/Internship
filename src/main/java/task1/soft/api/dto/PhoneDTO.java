package task1.soft.api.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import task1.soft.api.entity.PhoneType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Data
public class PhoneDTO {

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private PhoneType type;

    @NotEmpty
    private String number;

}
