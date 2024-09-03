package swp.todoapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swp.todoapp.model.TodoItem;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {
    public List<TodoItem> findByIsDeleted(boolean isDeleted);
}
