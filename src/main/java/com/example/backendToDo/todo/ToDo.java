package com.example.backendToDo.todo;

import java.time.LocalDate;

public class ToDo {
    public int id;
    public String name;
    public LocalDate dueDate;
    public boolean isDone;
    public LocalDate doneDate;
    public Priority priority;
    public LocalDate creationDate;

    public ToDo() {

        id = -1;
        name = "";
        dueDate = null;
        isDone = false;
        doneDate = null;
        priority = Priority.Low;
        creationDate = LocalDate.now();

    }

    public ToDo(int id, String name, LocalDate dueDate, boolean isDone, LocalDate doneDate) {

        this.id = id;
        this.name = name;
        this.dueDate = dueDate;
        this.isDone = isDone;
        this.doneDate = doneDate;
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