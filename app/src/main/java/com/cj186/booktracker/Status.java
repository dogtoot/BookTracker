package com.cj186.booktracker;

public enum Status {
    CURRENTLY_READING("Currently Reading"),
    REREADING("Re-Reading"),
    PLANNING_TO_READ("Planning to Read"),
    COMPLETED("Completed");

    private final String label;

    Status(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
