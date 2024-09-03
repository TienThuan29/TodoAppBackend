package swp.todoapp.dto.respone;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
public class ResponseSuccess<T> extends ResponseEntity<ResponseSuccess.Payload<T>>{
    public ResponseSuccess(String message) {
        super(new Payload(message), HttpStatus.OK);
    }

    public ResponseSuccess(String message, T data) {
        super(new Payload(message, data), HttpStatus.OK);
    }

    public static class Payload<T> {
        private final String message;
        private T data;

        public Payload(String message) {
            this.message = message;
        }

        public Payload(String message, T data) {
            this.message = message;
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public T getData() {
            return data;
        }
    }
}
