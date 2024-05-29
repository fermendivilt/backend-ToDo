package com.example.backendToDo.todo;

import java.time.LocalDateTime;

public class ToDo {

    public int id = -1;
    public String name = "";
    public Priority priority = Priority.NONE;
    public boolean isDone = false;

    // Respect ISO 8601 spec, use LocalDateTime
    public String dueDate = null;
    public String doneDate = null;
    public String creationDate = null;

    public ToDo() {
    }

    public ToDo(String name, Priority priority, LocalDateTime creationDate, LocalDateTime dueDate) {

        this.name = name;
        this.priority = priority;
        this.creationDate = creationDate.toString();
        if (dueDate != null)
            this.dueDate = dueDate.toString();

    }

    // public int getId() {
    // return id;
    // }
    // public void setId(int id) {
    // this.id = id;
    // }

    // public String getName() {
    // return this.name;
    // }
    // public void setName(String name) {
    // this.name = name;
    // }

    // public LocalDateTime getDueDate() {
    // return this.dueDate;
    // }
    // public void setDueDate(LocalDateTime dueDate) {
    // this.dueDate = dueDate;
    // }

    public ToDo UpdateData(ToDo toDo){

        this.name = toDo.name;
        this.priority = toDo.priority;
        this.dueDate = toDo.dueDate;
        this.isDone = toDo.isDone;
        this.doneDate = toDo.doneDate;

        return this;
    }

    public void SetIsDone(boolean isDone) {
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
                this.doneDate = LocalDateTime.now().toString();
            }
        }
    }

    public static boolean FormateableDateTime(String date) {
        try {
            LocalDateTime.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}