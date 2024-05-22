package com.example.backendToDo.todo;

import java.time.LocalDate;

public class ToDo {
    public int id;
    public String name;
    public LocalDate dueDate;
    public boolean isDone = false;
    public LocalDate doneDate = null;
    public Priority priority;
    public LocalDate creationDate;

    public ToDo() {

        id = -1;
        name = "";
        dueDate = null;
        priority = Priority.Low;
        creationDate = LocalDate.now();

    }

    public ToDo(String name, LocalDate dueDate) {

        this.name = name;
        this.dueDate = dueDate;
        creationDate = LocalDate.now();

    }

    // public int getId() {
    //     return id;
    // }
    // public void setId(int id) {
    //     this.id = id;
    // }
    
    // public String getName() {
    //     return this.name;
    // }
    // public void setName(String name) {
    //     this.name = name;
    // }

    // public LocalDate getDueDate() {
    //     return this.dueDate;
    // }
    // public void setDueDate(LocalDate dueDate) {
    //     this.dueDate = dueDate;
    // }



    public void setIsDone(boolean isDone) {
        if (this.isDone) {
            // from done to undone
            if (!isDone) {
                this.isDone = false;
                this.doneDate = null;
            }
        } else {
            // from undone to done
            if (isDone) {
                this.isDone = true;
                this.doneDate = LocalDate.now();
            }
        }
    }

}