package task1.soft.api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@AllArgsConstructor
public class NotFoundException extends RuntimeException {

    private String message;

}
