package task1.soft.api.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
public class NoDeletePermissionException extends RuntimeException {

    private String message;

    public NoDeletePermissionException(String message) {
        this.message=message;

    }
}
