package com.example;

public class Room {

    public Room(String n, String d, Direction[] dr, String[] i){
        name=n;
        description=d;
        directions=dr;
        items=i;

    }

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
