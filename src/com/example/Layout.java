package com.example;

public class Layout {

    public Layout(){}

    private String startingRoom;
    private String endingRoom;
    private Room[] rooms;
    private Monster[] monsters;
    private Player player;

    public Monster[] getMonsters() {
        return monsters;
    }

    public Player getPlayer() {
        return player;
    }

    public String getStartingRoom() {
        return startingRoom;
    }

    public String getEndingRoom() {
        return endingRoom;
    }

    public Room[] getRooms() {
        return rooms;
    }
}
