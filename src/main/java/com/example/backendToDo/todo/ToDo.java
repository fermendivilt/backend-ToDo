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
    public String creationDate = LocalDateTime.now().toString();

    public ToDo() {
    }

    public ToDo(String name, Priority priority, LocalDateTime dueDate) {

        this.name = name;
        this.priority = priority;
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
                this.doneDate = LocalDateTime.now().toString();
            }
        }
    }

    public static String FormatDateTime(LocalDateTime date) {

        if(date == null) return LocalDateTime.now().toString();

        return date.toString();
    }   

}