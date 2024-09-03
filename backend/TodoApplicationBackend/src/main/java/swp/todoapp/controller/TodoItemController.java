package swp.todoapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import swp.todoapp.dto.info.TodoItemDTO;
import swp.todoapp.dto.respone.ResponseSuccess;
import swp.todoapp.exception.def.NotFoundException;
import swp.todoapp.service.TodoItemService;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class TodoItemController {

    @Autowired
    private TodoItemService itemService;

    @PostMapping("/create")
    public ResponseSuccess<Object> createItem(@RequestBody TodoItemDTO itemDTO){
        itemService.saveItem(itemDTO);
        return new ResponseSuccess<>("success");
    }

    @PutMapping("/update/{id}")
    public ResponseSuccess<Object> updateItem
            (@PathVariable Long id, @RequestBody TodoItemDTO itemDTO)
            throws NotFoundException
    {
        itemService.updateItem(id,itemDTO);
        return new ResponseSuccess<>("success");
    }

    @PutMapping("/done/{id}")
    public ResponseSuccess<Object> markDone(@PathVariable Long id)
            throws NotFoundException
    {
        itemService.markDone(id);
        return new ResponseSuccess<>("success");
    }

    @DeleteMapping("/logic/{id}")
    public ResponseSuccess<Object> deleteLogic(@PathVariable Long id)
            throws NotFoundException
    {
        itemService.deleteLogic(id);
        return new ResponseSuccess<>("success");
    }

    @DeleteMapping("/delete/hard/{id}")
    public ResponseSuccess<Object> deleteHard(@PathVariable Long id)
            throws NotFoundException
    git {
        itemService.deleteHard(id);
        return new ResponseSuccess<>("success");
    }

    @DeleteMapping("/get-all")
    public ResponseSuccess<List<TodoItemDTO>> getAll(@RequestParam(name = "deleted") String isDeleted){
        List<TodoItemDTO> list= itemService.getAll(isDeleted);
        if(list.isEmpty()){
            return new ResponseSuccess<>("nothing");
        }
        return new ResponseSuccess<>("success", null);
    }
}
