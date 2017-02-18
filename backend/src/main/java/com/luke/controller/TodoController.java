package com.luke.controller;

import com.luke.entity.TodoEntity;
import com.luke.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

//!!! Look at rest_service_documentation.txt for REST service definitions !!!

@RestController
@CrossOrigin
public class TodoController {

    @Autowired
    TodoService todoService;

    @RequestMapping(method = RequestMethod.GET, path = "/todo")
    public List<TodoEntity> getAll(){
        return todoService.getAll();
    }

    @RequestMapping(method = RequestMethod.POST, path = "/new")
    public boolean add(@Valid @RequestBody TodoEntity entity){
        return todoService.add(entity);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/edit")
    public boolean edit(@Valid @RequestBody TodoEntity entity){
        return todoService.edit(entity);
    }

    //It wasn't mentioned in the exercise, but I added it for convenience
    @RequestMapping(method = RequestMethod.DELETE, path = "/delete/{id}")
    public boolean delete(@PathVariable("id") long id){
        return todoService.delete(id);
    }

    //It would be better to return an object containing for ex. list of field names and error messages,
    //but I return a boolean in accordance with the description of exercise
    @ExceptionHandler
    public boolean handleValidationException(MethodArgumentNotValidException exception){
        return false;
    }

}