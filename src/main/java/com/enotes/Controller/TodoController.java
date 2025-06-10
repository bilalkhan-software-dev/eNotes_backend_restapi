package com.enotes.Controller;

import com.enotes.Dto.TodoDto;
import com.enotes.Endpoints.TodoControllerEndpoints;
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
@RequiredArgsConstructor
public class TodoController implements TodoControllerEndpoints {

    private final TodoService todoService;


    @Override
    public ResponseEntity<?> addTodo(TodoDto todoDto) {

        boolean isAdded = todoService.addTodo(todoDto);

        if (isAdded) {
            return CommonUtil.createBuildResponseMessage("Todo Added Successfully!", HttpStatus.CREATED);
        }

        return CommonUtil.createErrorResponseMessage("Adding todo failed!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> getTodoById(Integer todoId) throws ResourceNotFoundException {

        TodoDto isAvailable = todoService.getTodoById(todoId);

        return CommonUtil.createBuildResponse(isAvailable, HttpStatus.OK);

    }


    @Override
    public ResponseEntity<?> getAllTodoByUser() {

        List<TodoDto> todoByUser = todoService.getTodoByUser();

        if (CollectionUtils.isEmpty(todoByUser)) {
            return CommonUtil.createErrorResponseMessage("Your Todo is empty!", HttpStatus.OK);
        }
        return CommonUtil.createBuildResponse(todoByUser, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> updateTodoStatus(Integer id, TodoDto todoDto) throws ResourceNotFoundException {

        boolean isUpdated = todoService.updateStatus(id, todoDto);

        if (isUpdated) {
            return CommonUtil.createBuildResponseMessage("Todo updated Successfully!", HttpStatus.CREATED);
        }

        return CommonUtil.createErrorResponseMessage("Todo update failed!", HttpStatus.BAD_REQUEST);
    }


}
