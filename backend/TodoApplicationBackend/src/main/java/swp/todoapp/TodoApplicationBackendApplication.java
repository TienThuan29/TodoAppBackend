package swp.todoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import swp.todoapp.model.TodoItem;
import swp.todoapp.repository.TodoItemRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class TodoApplicationBackendApplication {


    public static void main(String[] args) throws ParseException {
        ApplicationContext context = SpringApplication.run(TodoApplicationBackendApplication.class, args);

    }

}
