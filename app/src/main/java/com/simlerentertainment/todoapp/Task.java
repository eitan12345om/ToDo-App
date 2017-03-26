package com.simlerentertainment.todoapp;

/**
 * Created by Eitan on 3/12/2017.
 */

// TODO: Implement Task Class

public class Task {

    // Instance variables
    private int ID;
    private String description;
    private String date;


    // Constructors
    public Task(int ID, String description) {
        this.ID = ID;
        this.description = description;
    }

    public Task(int ID, String description, String date) {
        this.ID = ID;
        this.description = description;
        this.date = date;
    }
}
