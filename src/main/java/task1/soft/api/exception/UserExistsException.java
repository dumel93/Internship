package task1.soft.api.exception;

import lombok.Data;

@Data
public class UserExistsException extends RuntimeException {

    private String message;

    public UserExistsException(String message) {

        this.message = message;
    }
}
