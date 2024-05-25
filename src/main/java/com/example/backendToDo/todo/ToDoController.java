package com.example.backendToDo.todo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ToDoController {

    private final ToDoService toDoService;

    //Autowired injects dependencies during object creation (this case)
    //  Also works in setters or directly in fields
    @Autowired
    public ToDoController(ToDoService service){
        this.toDoService = service;
    }

    @GetMapping("/todos")
    public List<ToDo> getToDos(@RequestBody GetAllOptions options){
        return toDoService.GetAll(options);
    }

    @PostMapping("/todos")
    public ToDo postToDo(@RequestBody ToDo dto){
        return toDoService.Post(dto);
    }
}