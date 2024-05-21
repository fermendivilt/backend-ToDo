package com.example.backendToDo.todo;

import java.util.ArrayList;
import java.util.List;

public class ToDoService implements ToDoRepository {
    // We want a singleton for toDoList be the same across the app
    private static ToDoService instance;
    private List<ToDo> toDoList = new ArrayList<ToDo>();

    // To prevent direct instantiation
    private ToDoService() {}

    // Singleton creation or safe obtention
    public static ToDoService getInstance() {
        if(instance == null){

            // esto asegura la seguridad de los hilos?
            synchronized (ToDoService.class){

                if(instance == null){
                
                    instance = new ToDoService();
                }
            }
        }

        return instance;
    }

    @Override
    public String GetAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'GetAll'");
    }

    @Override
    public ToDo Post() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Post'");
    }

    @Override
    public ToDo PostDone() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'PostDone'");
    }

    @Override
    public ToDo Put() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Put'");
    }

    @Override
    public ToDo PutUndone() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'PutUndone'");
    }
}
