package com.example.backendToDo.todo;

import java.util.List;

public class GetAllResponseDTO {
    public List<ToDo> toDos = null;
    public int pages = 0;

    public GetAllResponseDTO(){}
    public GetAllResponseDTO(List<ToDo> toDos, int pages){
        this.toDos = toDos;
        this.pages = pages;
    }
}
