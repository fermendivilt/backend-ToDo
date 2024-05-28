package com.example.backendToDo.todo;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ToDoService {

    private final ToDoRepository repository;

    @Autowired
    private ToDoService(InMemoryToDoRepository repository) {
        this.repository = repository;
    }

    public List<ToDo> GetAll(GetAllOptions options) {

        Stream<ToDo> result = this.repository.GetAll().stream();

        if (options.stateFilter != GetAllStateFilter.NONE)
            result = result.filter(toDo -> toDo.isDone == (options.stateFilter == GetAllStateFilter.DONE));

        if (options.priorityFilter != Priority.NONE)
            result = result.filter(toDo -> toDo.priority == options.priorityFilter);

        if (options.nameFilter != null && options.nameFilter.length() > 0)
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

        return result.toList();
    }

    public ToDo Post(ToDo newToDo) {
        if (newToDo.name == null ||
                newToDo.name.length() < 1 || newToDo.name.length() > 120 ||
                newToDo.priority == null ||
                newToDo.creationDate == null ||
                !ToDo.FormateableDateTime(newToDo.creationDate) ||
                (newToDo.dueDate != null &&
                        !ToDo.FormateableDateTime(newToDo.dueDate)))
            return null;

        return this.repository.SaveNew(newToDo);
    }

    public ToDo PostDone(int id) {
        List<ToDo> todos = this.repository.GetAll();

        if(id < 0 || id >= todos.size()) return null;

        ToDo result = todos.get(id);
        result.SetIsDone(true);

        return this.repository.SaveChanges(id, result);
    }

    public ToDo Put(int id, ToDo toDo) {
        List<ToDo> todos = this.repository.GetAll();

        if (id < 0 || id >= todos.size() ||
                toDo.name == null || toDo.name.length() > 120 ||
                toDo.priority == null ||
                (toDo.dueDate != null &&
                    !ToDo.FormateableDateTime(toDo.dueDate)))
            return null;

        return this.repository.SaveChanges(id, toDo);
    }

    public ToDo PutUndone(int id) {
        List<ToDo> todos = this.repository.GetAll();

        if(id < 0 || id >= todos.size()) return null;

        ToDo result = todos.get(id);
        result.SetIsDone(false);

        return this.repository.SaveChanges(id, result);
    }
}
