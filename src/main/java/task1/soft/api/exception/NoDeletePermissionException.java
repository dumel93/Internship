package task1.soft.api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NoDeletePermissionException extends RuntimeException {

    private String message;

}
