package swp.todoapp.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.todoapp.dto.info.TodoItemDTO;
import swp.todoapp.dto.info.UserDTO;
import swp.todoapp.exception.def.NotFoundException;
import swp.todoapp.mapper.TodoItemMapper;
import swp.todoapp.model.TodoItem;
import swp.todoapp.model.User;
import swp.todoapp.repository.TodoItemRepository;
import swp.todoapp.repository.UserRepository;
import swp.todoapp.utils.convertDataUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TodoItemService {

    @Autowired
    private TodoItemRepository todoItemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    public TodoItemDTO saveItem(String name, String endDate, String description,
                        HttpServletRequest request, HttpServletResponse response) throws ParseException {
        UserDTO userDTO= userService.getUserInfo(request,response);
        LocalDateTime currentDateTime = LocalDateTime.now();
        Date createDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());

        TodoItem item= TodoItem.builder()
                .user(userRepository.findByUsername(userDTO.getUsername()).get())
                .name(name)
                .createdDate(createDate)
                .endDate(convertDataUtil.date(endDate))
                .description(description)
                .build();
        return TodoItemMapper.toItemDTO(
                todoItemRepository.save(item)
        );
    }

    public TodoItemDTO updateItem(Long id,TodoItemDTO updateItem) throws NotFoundException, ParseException {
        Optional<TodoItem> itemOptional= todoItemRepository.findById(id);
        if(!itemOptional.isPresent()){
            throw new NotFoundException("Item not found to update");
        }
        TodoItem item= itemOptional.get();

        item.setName(updateItem.getName());
        item.setCreatedDate(convertDataUtil.date(updateItem.getCreatedDate()));
        item.setEndDate(convertDataUtil.date(updateItem.getCreatedDate()));
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
