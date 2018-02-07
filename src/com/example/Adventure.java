package com.example;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.ArrayList;
import java.util.Arrays;
import java.net.MalformedURLException;
import java.util.Scanner;

public class Adventure {

    private static Room currentRoom = new Room();
    private static ArrayList<String> currentItems;
    private static ArrayList<String> myItems;
    private static Layout layout;
    private static Room[] rooms;
    private static String originalInput;
    private static boolean gameIsOver=false;

    public static void load(String url) {
        try {
            layout = Main.makeApiRequest(url);
        } catch (UnirestException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void start(Layout l) {
        layout = l;
        myItems = new ArrayList<>();
        rooms = layout.getRooms();
        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i].getName().equals(layout.getStartingRoom())) {
                currentRoom = rooms[i];
            }
        }
        itemsToArray();
        statusReport();
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

        System.out.println("The directions you can move are: ");
        for (Direction e: currentRoom.getDirections()) {
            System.out.println(e.getDirectionName());
        }
    }

    public static void inputEvaluator(String input) {
        if (input == null) {
            System.out.println("Enter input");
        }

        if (input.equals("exit") || input.equals("quit")) {
            gameIsOver = true;
            System.exit(0);
        } else if (input.contains("go")) {
            boolean invalidInput = true;
            for (int i = 0; i < currentRoom.getDirections().length; i++){
                //making sure input is an option
                if (input.contains(currentRoom.getDirections()[i].getDirectionName().toLowerCase())) {
                    invalidInput = false;
                    System.out.println(originalInput);
                    move(currentRoom.getDirections()[i].getRoom());
                    break;
                }
            }
            if(invalidInput) {
                System.out.println("I can't go that direction");
            }
        } else if (input.contains("take")) {
            boolean invalidInput = true;
            for (int i = 0; i < currentItems.size(); i++){
                //making sure input is an option
                if (input.contains(currentItems.get(i))) {
                    invalidInput = false;
                    System.out.println(originalInput);
                    take(currentItems.get(i));
                    break;
                }
            }
            if(invalidInput) {
                System.out.println("I can't take that item");
            }
        } else if (input.contains("drop")) {
            boolean invalidInput = true;
            for (int i = 0; i < myItems.size(); i++) {
                //making sure input is an option
                if (input.contains(myItems.get(i))) {
                    invalidInput = false;
                    System.out.println(originalInput);
                    drop(myItems.get(i));
                    break;
                }
            }
            if (invalidInput) {
                System.out.println("I can't drop that item");
            }
        }
        statusReport();
    }

    public static void drop(String item) {
        myItems.remove(item);
        currentItems.add(item);
    }

    public static void itemsToArray() {
        if (currentRoom.getItems() != null) {
            currentItems = new ArrayList<>(Arrays.asList(currentRoom.getItems()));
        }else {
            currentItems = new ArrayList<>();
        }
    }

    public static void take(String item) {
        myItems.add(item);
        currentItems.remove(item);
        for (int i = 0;  i <currentItems.size(); i++) {
            if(currentItems.get(i).equals(item)) {
                currentItems.remove(i);
            }
        }
    }

    public static void move(String room) {
       for(int i = 0; i < rooms.length; i++) {
           if (rooms[i].getName().equals(room)) {
               currentRoom = rooms[i];
               itemsToArray();
               break;
           }
       }
    }

    /**
     * https://www.geeksforgeeks.org/command-line-arguments-in-java/
     * @param args
     */
    public static void main(String[] args) {
        load("https://courses.engr.illinois.edu/cs126/adventure/siebel.json");
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
