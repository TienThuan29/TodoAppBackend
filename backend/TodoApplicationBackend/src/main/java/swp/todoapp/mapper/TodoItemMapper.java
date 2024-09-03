package swp.todoapp.mapper;

import swp.todoapp.dto.info.TodoItemDTO;
import swp.todoapp.model.TodoItem;

public class TodoItemMapper {
    public static TodoItemDTO toItemDTO(TodoItem item){
        return TodoItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .createdDate(item.getCreatedDate())
                .endDate(item.getEndDate())
                .description(item.getDescription())
                .isDone(item.isDone())
                .isDeleted(item.isDeleted())
                .build();
    }

    public static TodoItem toItem(TodoItemDTO item){
        return TodoItem.builder()
                .id(item.getId())
                .name(item.getName())
                .createdDate(item.getCreatedDate())
                .endDate(item.getEndDate())
                .description(item.getDescription())
                .isDone(item.isDone())
                .isDeleted(item.isDeleted())
                .build();
    }
}
