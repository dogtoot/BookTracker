package com.cj186.booktracker.model;

public enum Filter {
    CURRENTLY_READING("Currently Reading"),
    REREADING("Re-Reading"),
    PLANNING_TO_READ("Planning to Read"),
    COMPLETED("Completed"),
    ALL("All");

    private final String label;

    Filter(String label) {
        this.label = label;
    }

    public String getLabel() {
        // Get the label.
        return label;
    }
    public static Filter fromLabel(String label) {
        // Get the Status from a label.
        for (Filter filter : Filter.values()) {
            if (filter.label.equals(label)) {
                return filter;
            }
        }
        throw new IllegalArgumentException("Unknown filter label: " + label);
    }
}
