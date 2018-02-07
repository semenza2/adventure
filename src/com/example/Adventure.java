package com.example;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.codec.binary.StringUtils;

import java.net.MalformedURLException;
import java.util.Scanner;

public class Adventure {

    private static Room currentRoom;
    private static Layout layout;
    private static Room[] rooms = layout.getRooms();
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
        start(layout);
    }

    public static void start(Layout l) {
        layout = l;
        Room[] rooms = layout.getRooms();
        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i].getName() == layout.getStartingRoom()) {
                currentRoom = rooms[i];
            }
        }
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
        }

        if(currentRoom.getItems().length == 0) {
            System.out.println("This room contains nothing");
        } else {
            System.out.println("The current items in your room are: ");
            for (String e : currentRoom.getItems()) {
                System.out.println(e);
            }
        }

        System.out.println("The directions you can move are: ");
        for (Direction e: currentRoom.getDirections()) {
            System.out.println(e);
        }
    }

    public static void inputEvaluator(String input) {
        if (input == null) {
            System.out.println("Enter input");
        }

        if (input.equals("exit") || input.equals("quit")) {
            gameIsOver = true;
            System.exit(0);
        }
        if (input.contains("go")) {
            boolean invalidInput = true;
            for (int i = 0; i < currentRoom.getDirections().length; i++){
                //making sure input is an option
                if (input.contains(currentRoom.getDirections()[i].getDirectionName())) {
                    invalidInput = false;
                    System.out.println(originalInput);
                    move(currentRoom.getDirections()[i].getRoom());
                    break;
                }
            }
            if(invalidInput) {
                System.out.println("I can't go that direction");
            }
        }
    }

    public static void move(String room) {
       for(int i = 0; i < rooms.length; i++) {
           if (rooms[i].getName().equals(room)) {
               currentRoom = rooms[i];
               statusReport();
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
