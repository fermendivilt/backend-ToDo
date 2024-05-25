package com.example.backendToDo.todo;

import java.time.LocalDate;
import java.util.List;

public interface ToDoRepository {
    public List<ToDo> GetAll(GetAllOptions options);
    public ToDo Post(ToDo newToDo);
    public ToDo PostDone(int id);
    public ToDo Put(int id, String name, LocalDate dueDate, Priority priority);
    public ToDo PutUndone(int id);
}
