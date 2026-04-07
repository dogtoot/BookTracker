package com.cj186.booktracker.model;

/**
 * Collin J. Johnson
 * 5/6/2025
 * 2376 Mobile Applications Development
 *
 * This enum holds the statuses the user can select.
 */

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
        // Get the label.
        return label;
    }
    public static Status fromLabel(String label) {
        // Get the Status from a label.
        for (Status status : Status.values()) {
            if (status.label.equals(label)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status label: " + label);
    }
}
