package swp.todoapp.mapper;

import swp.todoapp.dto.info.TodoItemDTO;
import swp.todoapp.model.TodoItem;
import swp.todoapp.utils.convertDataUtil;

import java.text.ParseException;

public class TodoItemMapper {
    public static TodoItemDTO toItemDTO(TodoItem item){
        return TodoItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .createdDate(item.getCreatedDate().toString())
                .endDate(item.getEndDate().toString())
                .description(item.getDescription())
                .isDone(item.isDone())
                .isDeleted(item.isDeleted())
                .build();
    }

    public static TodoItem toItem(TodoItemDTO item) throws ParseException {
        return TodoItem.builder()
                .id(item.getId())
                .name(item.getName())
                .createdDate(convertDataUtil.date(item.getCreatedDate()))
                .endDate(convertDataUtil.date(item.getEndDate()))
                .description(item.getDescription())
                .isDone(item.isDone())
                .isDeleted(item.isDeleted())
                .build();
    }
}
