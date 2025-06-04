package com.enotes.Service.Impl;

import com.enotes.Entity.Todo;
import com.enotes.Dto.TodoDto;
import com.enotes.Dto.TodoDto.StatusDto;
import com.enotes.Exception.ResourceNotFoundException;
import com.enotes.Repository.TodoRepository;
import com.enotes.Service.TodoService;
import com.enotes.Util.TodoStatus;
import com.enotes.Util.Validation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final Validation validation;
    private final ModelMapper modelMapper;

    @Override
    public boolean addTodo(TodoDto todoDto) {


        validation.TodoValidation(todoDto);
        Todo todo = modelMapper.map(todoDto, Todo.class);

        todo.setStatusId(todoDto.getStatus().getId());

        Todo isSaved = todoRepository.save(todo);

        return !ObjectUtils.isEmpty(isSaved);
    }

    @Override
    public TodoDto getTodoById(Integer id) throws ResourceNotFoundException {

        Todo todo = todoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Invalid todo id  | todo not found!")
        );

        TodoDto todoDto = modelMapper.map(todo, TodoDto.class);

        setStatus(todoDto, todo);

        return todoDto;
    }


    // This function is used for set name
    public void setStatus(TodoDto todoDto, Todo todo) {

        for (TodoStatus st : TodoStatus.values()) {

            if (st.getId().equals(todo.getStatusId())) {

                StatusDto statusDto = StatusDto.builder()
                        .id(st.getId())
                        .name(st.getName())
                        .build();
                todoDto.setStatus(statusDto);

            }
        }
    }

    @Override
    public List<TodoDto> getTodoByUser() {

        Integer userId = 1;

        List<Todo> todoList = todoRepository.findByCreatedBy(userId);
        return todoList.stream().map(
                todo -> {
                    // set status name
                    TodoDto todoDto = modelMapper.map(todo, TodoDto.class);
                    setStatus(todoDto, todo);
                    return todoDto;
                }
        ).toList();
    }

    @Override
    public boolean updateStatus(Integer id, TodoDto todoDto) throws ResourceNotFoundException {

        Todo existingTodo = todoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Invalid todo id!")
        );


        validation.TodoValidation(todoDto);

        // skip nullable from the dto
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(todoDto, existingTodo);

        existingTodo.setStatusId(todoDto.getStatus().getId());


        Todo isUpdated = todoRepository.save(existingTodo);

        return !ObjectUtils.isEmpty(isUpdated);
    }
}
