package swp.todoapp.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import swp.todoapp.exception.def.ErrorResponse;
import swp.todoapp.exception.def.UsernamePasswordInvalidException;

@RestControllerAdvice
public class UserPasswordInvalidExceptionHandler extends RuntimeException {

    @ExceptionHandler(UsernamePasswordInvalidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handle(UsernamePasswordInvalidException exception) {
        return new ErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

}
