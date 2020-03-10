package task1.soft.api.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@Data
public class NotFoundException extends RuntimeException {
    private String message;

    public NotFoundException(String not_found) {
        this.message=not_found;
    }
}
