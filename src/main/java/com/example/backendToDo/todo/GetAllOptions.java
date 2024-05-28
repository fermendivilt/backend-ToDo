package com.example.backendToDo.todo;

public class GetAllOptions {

    public int pageNumber = 1;
    public GetAllStateFilter stateFilter = GetAllStateFilter.NONE;
    public Priority priorityFilter = Priority.NONE;
    public String nameFilter = null;
    public GetAllSortingDirection sortingDueDate = GetAllSortingDirection.NONE;
    public GetAllSortingDirection sortingPriority = GetAllSortingDirection.NONE;
    
}

