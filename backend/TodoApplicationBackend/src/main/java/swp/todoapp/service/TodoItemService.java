package swp.todoapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.todoapp.dto.info.TodoItemDTO;
import swp.todoapp.exception.def.NotFoundException;
import swp.todoapp.mapper.TodoItemMapper;
import swp.todoapp.model.TodoItem;
import swp.todoapp.repository.TodoItemRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TodoItemService {

    @Autowired
    private TodoItemRepository todoItemRepository;

    public TodoItemDTO saveItem(TodoItemDTO todoItemDTO){
        java.util.Date date = java.sql.Date.valueOf(java.time.LocalDate.now());
        todoItemDTO.setCreatedDate(date);
        return TodoItemMapper.toItemDTO(
            todoItemRepository.save(TodoItemMapper.toItem(todoItemDTO))
        );
    }

    public TodoItemDTO updateItem(Long id,TodoItemDTO updateItem)throws NotFoundException{
        Optional<TodoItem> itemOptional= todoItemRepository.findById(id);
        if(!itemOptional.isPresent()){
            throw new NotFoundException("Item not found to update");
        }
        TodoItem item= itemOptional.get();
        item.setName(updateItem.getName());
        item.setCreatedDate(updateItem.getCreatedDate());
        item.setEndDate(updateItem.getEndDate());
        item.setDescription(updateItem.getDescription());
        todoItemRepository.save(item);
        return updateItem;
    }

    public TodoItemDTO markDone(Long id)throws NotFoundException{
        Optional<TodoItem> itemOptional= todoItemRepository.findById(id);
        if(!itemOptional.isPresent()){
            throw new NotFoundException("Item not found to mark done");
        }
        TodoItem item= itemOptional.get();
        item.setDone(true);
        return TodoItemMapper.toItemDTO(todoItemRepository.save(item));
    }

    public TodoItemDTO deleteLogic(Long id)throws NotFoundException{
        Optional<TodoItem> itemOptional= todoItemRepository.findById(id);
        if(!itemOptional.isPresent()){
            throw new NotFoundException("Item not found to delete");
        }
        TodoItem item= itemOptional.get();
        item.setDeleted(true);
        return TodoItemMapper.toItemDTO(todoItemRepository.save(item));
    }

    public void deleteHard(Long id) throws NotFoundException{
        Optional<TodoItem> itemOptional= todoItemRepository.findById(id);
        if(!itemOptional.isPresent()){
            throw new NotFoundException("Item not found to delete");
        }
        else
            todoItemRepository.deleteById(id);
    }

    public List<TodoItemDTO> getAll(String deleted){
        boolean isDeleted= Boolean.parseBoolean(deleted);

        List<TodoItem> listItem= todoItemRepository.findByIsDeleted(isDeleted);
        List<TodoItemDTO> listItemDTO=
                listItem.stream()
                        .map(TodoItemMapper::toItemDTO)
                        .collect(Collectors.toList());

        return listItemDTO;
    }
}
