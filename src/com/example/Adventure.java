package com.example;
import com.google.gson.Gson;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.ArrayList;
import java.util.Arrays;
import java.net.MalformedURLException;
import java.util.Scanner;

public class Adventure {

    private static Room currentRoom = new Room();
    private static ArrayList<String> currentItems;
    private static ArrayList<String> monstersInRoom;
    private static ArrayList<String> myItems;
    public static Layout layout;
    private static Room[] rooms;
    //keeps input as user enters it, to echo later
    private static String originalInput;
    private static boolean gameIsOver=false;

    /**
     * starts the game
     * @param l is the inputted layout
     */
    public static void start(Layout l) {
        layout = l;
        myItems = new ArrayList<>();
        rooms = layout.getRooms();
        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i].getName().equals(layout.getStartingRoom())) {
                currentRoom = rooms[i];
            }
        }
        itemsToArrayList();
        monstersInRoom();
        statusReport();
    }

    /**
     * allows use of an arrayList instead of an array to add and drop items
     */
    public static void itemsToArrayList() {
        if (currentRoom.getItems() != null) {
            currentItems = new ArrayList<>(Arrays.asList(currentRoom.getItems()));
        }else {
            currentItems = new ArrayList<>();
        }
    }

    public static void monstersInRoom() {
        if (currentRoom.getMonstersInRoom() != null) {
            monstersInRoom = new ArrayList<>(Arrays.asList(currentRoom.getMonstersInRoom()));
        }else {
            monstersInRoom = new ArrayList<>();
        }
    }

    /**
     * Describing the environment of the current room
     */
    public static void statusReport() {
        System.out.println(currentRoom.getDescription());
        if (currentRoom.getName().equals(layout.getStartingRoom())){
            System.out.println("Your journey begins here");
        } else if (currentRoom.getName().equals(layout.getEndingRoom())){
            System.out.println("You have reached your final destination");
            gameIsOver = true;
        }

        if(currentItems.size() == 0) {
            System.out.println("This room contains nothing");
        } else {
            System.out.println("The current items in your room are: ");
            for (String e : currentItems) {
                System.out.println(e);
            }
        }

        if(monstersInRoom.size() == 0) {
            System.out.println("This room has no monsters!");
        } else {
            System.out.println("The current monsters in your room are: ");
            for (String e : monstersInRoom) {
                System.out.println(e);
            }
        }
        System.out.println("The directions you can move are: ");
        for (Direction e: currentRoom.getDirections()) {
            System.out.println(e.getDirectionName());
        }
    }

    /**
     * evaluates and acts on the user's input
     * @param input the user's input in lowercase
     */
    public static void inputEvaluator(String input) {
        if (input == null) {
            System.out.println("Enter input");
        }
        if (input.equals("exit") || input.equals("quit")) {
            System.out.println(originalInput);
            gameIsOver = true;
            System.exit(0);
        } else if (input.startsWith("go")) {
           moveRooms(input);
        } else if (input.startsWith("take")) {
            take(input);
        } else if (input.startsWith("drop")) {
            drop(input);
        }
        statusReport();
    }

    /**
     * drop an item into room and remove from my own items
     * @param input the item to drop into the current room
     */
    public static void drop(String input) {
        input = input.substring(5);
        boolean invalidInput = true;
        for (int i = 0; i < myItems.size(); i++) {
            //making sure input is an option
            String item = myItems.get(i);
            if (input.equals(item)) {
                invalidInput = false;
                System.out.println(originalInput);
                currentItems.add(item);
                myItems.remove(item);
                break;
            }
        }
        if (invalidInput) {
            System.out.println("I can't drop that item");
        }
    }

    /**
     * takes item from current room and adds to my own items
     * @param input to take from room
     */
    public static void take(String input) {
        input = input.substring(5);
        boolean invalidInput = true;
        for (int i = 0; i < currentItems.size(); i++){
            //making sure input is an option
            String item = currentItems.get(i);
            if (input.contains(item)) {
                invalidInput = false;
                System.out.println(originalInput);
                myItems.add(item);
                currentItems.remove(item);
                break;
            }
        }
        if(invalidInput) {
            System.out.println("I can't take that item");
        }

    }

    /**
     * resets the current room to where the user chooses to go
     * @param input of where to move
     */
    public static void moveRooms(String input) {
        input = input.substring(3);
        boolean invalidInput = true;
        for (int i = 0; i < currentRoom.getDirections().length; i++){
            //making sure input is an option
            if (input.equals(currentRoom.getDirections()[i].getDirectionName().toLowerCase())) {
                invalidInput = false;
                System.out.println(originalInput);
                String newRoom = currentRoom.getDirections()[i].getRoom();
                for(int j = 0; j < rooms.length; j++) {
                    if (rooms[j].getName().equals(newRoom)) {
                        currentRoom = rooms[j];
                        itemsToArrayList();
                        monstersInRoom();
                        break;
                    }
                }
                break;
            }
        }
        if(invalidInput) {
            System.out.println("I can't go that direction");
        }
    }

    /**
     * main method that runs game
     * @param args
     */
    public static void main(String[] args) {
        String url = UrlLoader.getUrlContents("file:///Users/SarahMenza/Documents/withMonsters.txt");
        Gson gson = new Gson();
        layout = gson.fromJson(url, Layout.class);
        start(layout);
        Scanner scanner = new Scanner(System.in);
        while(!gameIsOver) {
            String input = scanner.nextLine();
            originalInput = input;
            inputEvaluator(input.toLowerCase());
        }
        if (args.length > 0) {
            System.out.print("The contents of my arguments are: ");
            for (String arg : args) {
                System.out.print("\"" + arg + "\" ");
            }
        } else {
            System.out.println("No command line "+
                    "arguments found.");
        }
    }
}
