package com.example.backendToDo.todo;

import java.util.List;

public interface ToDoRepository {
    public List<ToDo> GetAll();
    public ToDo SaveNew(ToDo newToDo);
    public ToDo SaveChanges(ToDo toDo);
}
