package com.example;

public class Room {

    public Room(){}

    private String name;
    private String description;
    private Direction[] directions;
    private String[] items;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Direction[] getDirections() {
        return directions;
    }

    public String[] getItems() {
        return items;
    }
}
