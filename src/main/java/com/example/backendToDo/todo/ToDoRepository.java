package com.example.backendToDo.todo;

public interface ToDoRepository {
    public String GetAll();
    public ToDo Post();
    public ToDo PostDone();
    public ToDo Put();
    public ToDo PutUndone();
}
