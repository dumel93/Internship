package task1.soft.api.exception;

import lombok.Data;

@Data
public class NoDeletePermissionException extends RuntimeException {

    private String message;

    public NoDeletePermissionException(String message) {
        this.message = message;

    }
}
