package com.example.backendToDo.todo;

import java.time.LocalDate;

import org.springframework.cglib.core.Local;

public class ToDo {
    private int id;
    private String name;
    private LocalDate dueDate;
    private boolean isDone;
    private LocalDate doneDate;
    private Priority priority;
    private LocalDate creationDate;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

enum Priority { Low, Medium, High }