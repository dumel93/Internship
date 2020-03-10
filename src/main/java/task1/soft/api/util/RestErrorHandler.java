package task1.soft.api.util;


import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import task1.soft.api.exception.Error;
import task1.soft.api.exception.NoDeletePermissionException;
import task1.soft.api.exception.NotFoundException;

@ControllerAdvice
class RestErrorHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    Error notFoundException(NotFoundException ex) {
        return new Error(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    @ExceptionHandler(NoDeletePermissionException.class)
    Error noDeletePermissionException(NoDeletePermissionException ex)  {
        return new Error(ex.getMessage());
    }
}
