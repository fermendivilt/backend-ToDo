package com.example.backendToDo.todo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.websocket.server.PathParam;

@CrossOrigin(origins = "http://localhost:8080")
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
    public GetAllResponseDTO getToDos(
        @PathParam("pageNumber") int pageNumber,
        @PathParam("priorityFilter") GetAllStateFilter stateFilter,
        @PathParam("nameFilter") String nameFilter,
        @PathParam("sortingDueDate") GetAllSortingDirection sortingDueDate,
        @PathParam("sortingPriority") GetAllSortingDirection sortingPriority        
        ){

        GetAllOptions options = new GetAllOptions();
        options.pageNumber = pageNumber;
        options.stateFilter = stateFilter;
        options.nameFilter = nameFilter;
        options.sortingDueDate = sortingDueDate;
        options.sortingPriority = sortingPriority;

        int elementsPerPage = 10;

        List<ToDo> result = toDoService.GetAll(options);
        int elements = result.size();

        result = result.stream()
            .skip((options.pageNumber - 1) * elementsPerPage).limit(elementsPerPage)
            .toList();

        return new GetAllResponseDTO(result, elements / elementsPerPage + 1);
    }

    @PostMapping("/todos")
    public ToDo postToDo(@RequestBody ToDo dto) {
        return toDoService.Post(dto);
    }

    @PostMapping("/todos/{id}/done")
    public ToDo postDone(@PathVariable("id") String id){
        return toDoService.PostDone(Integer.parseInt(id));
    }

    @PutMapping("/todos/{id}")
    public ToDo putToDo(@PathVariable("id") String id, @RequestBody ToDo dto){
        return toDoService.Put(Integer.parseInt(id), dto);
    }

    @PutMapping("/todos/{id}/undone")
    public ToDo putUndone(@PathVariable("id") String id){
        return toDoService.PutUndone(Integer.parseInt(id));
    }
}