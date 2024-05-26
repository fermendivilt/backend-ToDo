package com.example.backendToDo.todo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class InMemoryToDoRepository implements ToDoRepository {
    
    private List<ToDo> todos = new ArrayList<>();

    @Override
    public List<ToDo> GetAll() {
        return todos;
    }

    @Override
    public ToDo SaveNew(ToDo newToDo) {
        newToDo.id = this.GetAll().size();

        todos.add(newToDo);

        return todos.getLast();
    }

    @Override
    public ToDo SaveChanges(ToDo toDo){
        return todos.get(toDo.id).UpdateData(toDo);
    }
}