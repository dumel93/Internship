package task1.soft.api.exception;

import lombok.Data;


@Data
public class NotFoundException extends RuntimeException {

    private String message;

    public NotFoundException(String not_found) {

        this.message = not_found;
    }
}
