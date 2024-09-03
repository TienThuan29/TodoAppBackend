package swp.todoapp.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import swp.todoapp.exception.def.ErrorResponse;
import swp.todoapp.exception.def.InvalidTokenException;

@RestControllerAdvice
public class InvalidTokenEceptionHandler {

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse handle(InvalidTokenException exception) {
        return new ErrorResponse(HttpStatus.FORBIDDEN, exception.getMessage());
    }

}
