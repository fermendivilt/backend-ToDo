package com.example.backendToDo.todo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

@Component
public class ToDoService implements ToDoRepository {
    // We want a singleton for toDoList be the same across the app
    private static ToDoService instance;
    private List<ToDo> toDoList = new ArrayList<ToDo>();

    // To prevent direct instantiation
    private ToDoService() {
    }

    // Singleton creation or safe obtention
    public static ToDoService getInstance() {
        if (instance == null) {

            // esto asegura la seguridad de los hilos?
            synchronized (ToDoService.class) {

                if (instance == null) {

                    instance = new ToDoService();
                }
            }
        }

        return instance;
    }

    @Override
    public List<ToDo> GetAll(GetAllOptions options) {

        Stream<ToDo> result = toDoList.stream();

        if (options.stateFilter != GetAllStateFilter.NONE)
            result = result.filter(toDo -> toDo.isDone == (options.stateFilter == GetAllStateFilter.DONE));

        if (options.priorityFilter != null)
            result = result.filter(toDo -> toDo.priority == options.priorityFilter);

        if (options.nameFilter != null)
            result = result.filter(toDo -> toDo.name.contains(options.nameFilter));

        if (options.sortingDueDate != GetAllSortingDirection.NONE)
            if (options.sortingDueDate == GetAllSortingDirection.DESCENDING)
                result = result.sorted(Comparator.comparing(toDo -> LocalDateTime.parse(toDo.dueDate)));
            else
                result = result
                        .sorted(Comparator.comparing((ToDo toDo) -> LocalDateTime.parse(toDo.dueDate)).reversed());

        if (options.sortingPriority != GetAllSortingDirection.NONE)
            if (options.sortingPriority == GetAllSortingDirection.DESCENDING)
                result = result.sorted(Comparator.comparing(toDo -> toDo.priority));
            else
                result = result.sorted(Comparator.comparing((ToDo toDo) -> toDo.priority).reversed());

        result = result.skip((options.pageNumber - 1) * 10).limit(10);

        return result.toList();
    }

    @Override
    public ToDo Post(ToDo newToDo) {
        newToDo.id = toDoList.size();

        toDoList.addLast(newToDo);

        return toDoList.getLast();
    }

    @Override
    public ToDo PostDone(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'PostDone'");
    }

    @Override
    public ToDo Put(int id, String name, LocalDate dueDate, Priority priority) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Put'");
    }

    @Override
    public ToDo PutUndone(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'PutUndone'");
    }
}
