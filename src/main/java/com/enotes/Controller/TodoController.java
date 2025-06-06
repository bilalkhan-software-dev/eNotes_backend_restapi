package com.enotes.Controller;

import com.enotes.Dto.TodoDto;
import com.enotes.Exception.ResourceNotFoundException;
import com.enotes.Service.TodoService;
import com.enotes.Util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;


    @PostMapping("/add-todos")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addTodo(@RequestBody TodoDto todoDto) {

        boolean isAdded = todoService.addTodo(todoDto);

        if (isAdded) {
            return CommonUtil.createBuildResponseMessage("Todo Added Successfully!", HttpStatus.CREATED);
        }

        return CommonUtil.createErrorResponseMessage("Adding todo failed!", HttpStatus.INTERNAL_SERVER_ERROR);
     }

    @GetMapping("/dtls/{todoId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getTodoById(@PathVariable Integer todoId) throws ResourceNotFoundException {

        TodoDto isAvailable = todoService.getTodoById(todoId);

            return CommonUtil.createBuildResponse(isAvailable, HttpStatus.OK);

    }

    @GetMapping("/todos")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllTodoByUser() {

        List<TodoDto> todoByUser = todoService.getTodoByUser();

        if  (CollectionUtils.isEmpty(todoByUser)) {
            return CommonUtil.createErrorResponseMessage("Todo is empty", HttpStatus.NO_CONTENT);
        }
        return CommonUtil.createBuildResponse(todoByUser, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateTodoStatus(@PathVariable Integer id,@RequestBody TodoDto todoDto) throws ResourceNotFoundException {

       boolean isUpdated =   todoService.updateStatus(id,todoDto);

       if (isUpdated){
           return CommonUtil.createBuildResponseMessage("Todo updated Successfully!",HttpStatus.CREATED);
       }

        return CommonUtil.createErrorResponseMessage("Todo update failed!",HttpStatus.BAD_REQUEST);
    }


}
