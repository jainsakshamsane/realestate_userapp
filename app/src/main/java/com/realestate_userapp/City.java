package com.realestate_userapp;

import androidx.annotation.NonNull;

public class City {
    private String name;
    private String id;

    public City(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @NonNull
    @Override
    public String toString() {
        return name; // Display city name in Spinner
    }
}
