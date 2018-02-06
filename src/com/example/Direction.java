package com.example;

public class Direction {

    public Direction(String n, String r){
        directionName=n;
        room=r;
    }

    private String directionName;

    /**
     * name of the room the direction is point to
     */
    private String room;

    public String getDirectionName() {
        return directionName;
    }

    public String getRoom() {
        return room;
    }

}
