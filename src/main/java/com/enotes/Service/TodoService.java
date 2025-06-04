package com.enotes.Service;

import com.enotes.Dto.TodoDto;
import com.enotes.Exception.ResourceNotFoundException;

import java.util.List;

public interface TodoService {

    boolean addTodo(TodoDto todoDto);

    TodoDto getTodoById(Integer id) throws ResourceNotFoundException;

    List<TodoDto> getTodoByUser();

    boolean updateStatus(Integer id, TodoDto todoDto) throws ResourceNotFoundException;
}
