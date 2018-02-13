package com.example;

public class Player {
    private String name;
    private Item [] items;
    private double attack;
    private double defense;
    private double health;
    private int level;

    public String getName() {
        return name;
    }

    public Item[] getItems() {
        return items;
    }

    public double getAttack() {
        return attack;
    }

    public double getDefense() {
        return defense;
    }

    public double getHealth() {
        return health;
    }

    public int getLevel() {
        return level;
    }
}
