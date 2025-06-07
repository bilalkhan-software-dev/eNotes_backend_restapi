package com.enotes.Endpoints;


import com.enotes.Dto.TodoDto;
import com.enotes.Exception.ResourceNotFoundException;
import com.enotes.Util.AppConstant;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/todo")
public interface TodoControllerEndpoints {

    @PostMapping("/add-todos")
    @PreAuthorize(AppConstant.AUTH_ROLE_USER)
    public ResponseEntity<?> addTodo(@RequestBody TodoDto todoDto);


    @GetMapping("/dtls/{todoId}")
    @PreAuthorize(AppConstant.AUTH_ROLE_USER)
    public ResponseEntity<?> getTodoById(@PathVariable Integer todoId) throws ResourceNotFoundException;

    @GetMapping("/todos")
    @PreAuthorize(AppConstant.AUTH_ROLE_USER)
    public ResponseEntity<?> getAllTodoByUser();

    @PutMapping("/update/{id}")
    @PreAuthorize(AppConstant.AUTH_ROLE_USER)
    public ResponseEntity<?> updateTodoStatus(@PathVariable Integer id,@RequestBody TodoDto todoDto) throws ResourceNotFoundException;




}
