package com.simlerentertainment.todoapp;

/**
 * @author Eitan
 *
 * This class models a Task
 */

// TODO: Implement Task Class

public class Task {

    // Instance variables
    private int ID;
    private String description;
    private String date;

    /**
     * @return ID of Task
     */
    public int getID() {
        return ID;
    }

    /**
     * @param ID sets new ID
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * @return description of Task
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description sets new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return date of Task
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date sets new date
     */
    public void setDate(String date) {
        this.date = date;
    }
}
