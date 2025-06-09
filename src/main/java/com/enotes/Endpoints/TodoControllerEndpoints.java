package com.enotes.Endpoints;


import com.enotes.Dto.TodoDto;
import com.enotes.Exception.ResourceNotFoundException;
import com.enotes.Util.AppConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Todo",description = "Accessible only for user")
@RequestMapping("/api/v1/todo")
public interface TodoControllerEndpoints {

    @Operation(summary = "Add todo",tags = {"Todo","User"})
    @PostMapping("/add-todos")
    @PreAuthorize(AppConstant.AUTH_ROLE_USER)
    public ResponseEntity<?> addTodo(@RequestBody TodoDto todoDto);

    @Operation(summary = "Todo details by id ",tags = {"Todo","User"})
    @GetMapping("/dtls/{todoId}")
    @PreAuthorize(AppConstant.AUTH_ROLE_USER)
    public ResponseEntity<?> getTodoById(@PathVariable Integer todoId) throws ResourceNotFoundException;

    @Operation(summary = "User added todos",tags = {"Todo","User"})
    @GetMapping("/todos")
    @PreAuthorize(AppConstant.AUTH_ROLE_USER)
    public ResponseEntity<?> getAllTodoByUser();

    @Operation(summary = "Update todo",tags = {"Todo","User"})
    @PutMapping("/update/{id}")
    @PreAuthorize(AppConstant.AUTH_ROLE_USER)
    public ResponseEntity<?> updateTodoStatus(@PathVariable Integer id,@RequestBody TodoDto todoDto) throws ResourceNotFoundException;

}
