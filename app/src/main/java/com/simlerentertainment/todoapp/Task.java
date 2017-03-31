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

    // Constructor
    public Task(int ID, String description, String date) {
        this.ID = ID;
        this.description = description;
        this.date = date;
    }

    // Methods
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

    /**
     * @param other is the object to compare to
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (this.getClass() != other.getClass()) {
            return false;
        }

        Task o = (Task) other;

        return ID == o.ID && description.equals(o.description) && date.equals(o.date);
    }

    /**
     * @return description of task
     * TODO: Eventually delete this method
     */
    @Override
    public String toString() {
        return getDescription();
    }
}
