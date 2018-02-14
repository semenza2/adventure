package com.example;
import com.google.gson.Gson;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.ArrayList;
import java.util.Arrays;
import java.net.MalformedURLException;
import java.util.Scanner;

public class Adventure {

    private static Room currentRoom = new Room();
    private static ArrayList<Item> currentItems;
    private static ArrayList<String> monstersInRoom;
    private static ArrayList<Item> myItems;
    private static Layout layout;
    private static Player player = new Player();
    private static Room[] rooms;
    //keeps input as user enters it, to echo later
    private static String originalInput;
    private static boolean hasDisengaged;
    private static boolean gameIsOver=false;

    /**
     * starts the game
     * @param l is the inputted layout
     */
    public static void start(Layout l) {
        layout = l;
        myItems = new ArrayList<>();
        rooms = layout.getRooms();
        player = layout.getPlayer();
        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i].getName().equals(layout.getStartingRoom())) {
                currentRoom = rooms[i];
            }
        }
        playerItemsToArrayList();
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

    public static void playerItemsToArrayList() {
        if (player.getItems() == null) {
            myItems = new ArrayList<>();
        }else {
            myItems = new ArrayList<>(Arrays.asList(player.getItems()));
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
            System.out.println("Your journey begins here." + "\n");
        } else if (currentRoom.getName().equals(layout.getEndingRoom())){
            System.out.println("You have reached your final destination.");
            gameIsOver = true;
        }

        if(currentItems.size() == 0) {
            System.out.println("This room contains nothing.");
        } else {
            System.out.println("The current items in your room are: ");
            for (Item e : currentItems) {
                System.out.println(e.getName());
            }
            System.out.println();
        }

        if(monstersInRoom.size() == 0) {
            System.out.println("This room has no monsters!");
        } else {
            System.out.println("The current monsters in your room are: ");
            for (String e : monstersInRoom) {
                System.out.println(e);
            }
            System.out.println();
        }
        //if all monsters are defeated
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
        if (input == null || input.length() == 0) {
            System.out.println("Enter input");
            return;
        }
        if (input.equals("exit") || input.equals("quit")) {
            gameIsOver = true;
            System.exit(0);
        } else if (input.startsWith("go")) {
           moveRooms(input);
        } else if (input.startsWith("take")) {
            take(input);
        } else if (input.startsWith("drop")) {
            drop(input);
        } else if (input.equals("playerinfo")) {
            getPlayerInfo();
            return;
        } else if (input.equals("list")) {
            list();
            return;
        } else if (input.startsWith("duel")) {
            duel(input);
        } else {
            System.out.println("I don't understand '" + originalInput + "'");
        }
        statusReport();
    }

    public static void getPlayerInfo()
    {
        System.out.println("Your player is on level: " + player.getLevel());
        System.out.println("Attack: " + player.getAttack());
        System.out.println("Defense: " + player.getDefense());
        System.out.println("Health: " + player.getHealth());
    }


    public static void duel(String input) {
        if(input.length() > 4) {
            input = input.substring(5);
            boolean invalidInput = true;
            for (int i = 0; i < monstersInRoom.size(); i++) {
                //making sure input is an option
                String monster = monstersInRoom.get(i);
                if (input.equalsIgnoreCase(monster)) {
                    invalidInput = false;

                    //find equivalent monster object
                    Monster monsterInDuel = new Monster();
                    for (Monster m: layout.getMonsters()) {
                        if(monster.equalsIgnoreCase(m.getName())) {
                           monsterInDuel = m;
                        }
                    }
                    hasDisengaged = false;
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("You are dueling " + monster + "\n");
                    System.out.println("Please enter your first move.");

                    int numOfMonsters = monstersInRoom.size();
                    //if the monster was defeated, size of array will change
                    while (monstersInRoom.size() == numOfMonsters && hasDisengaged == false) {
                        String command = scanner.nextLine();
                        originalInput = command;
                        duelInputEvaluator(monsterInDuel, command.toLowerCase());
                    }
                    break;
                }
            }
            if (invalidInput) {
                System.out.println("I can't duel that monster" + "\n");
            }
        } else {
            System.out.println("Please specify which monster you would like to duel." + "\n");
        }
    }

    public static void duelInputEvaluator(Monster monster, String input) {
        if (input == null || input.length() == 0) {
            System.out.println("Enter input");
            return;
        }
        if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
            gameIsOver = true;
            System.exit(0);
        } else if (input.equalsIgnoreCase("attack")) {
            attack(monster);
        } else if (input.contains("attack with")) {
            attackWith(monster, input);
        } else if (input.equalsIgnoreCase("disengage")) {
            hasDisengaged = true;
        } else if (input.equalsIgnoreCase("status")) {
            duelStatus();
        } else if (input.equalsIgnoreCase("list")) {
            list();
        } else if (input.equalsIgnoreCase("playerinfo")) {
            getPlayerInfo();
        } else {
            System.out.println("I don't understand '" + originalInput + "'");
        }
    }

    public static void attack(Monster monster) {
        double damage = player.getAttack() - monster.getDefense();
        monster.setHealth(monster.getHealth() - damage);
        if (monster.getHealth() <= 0) {
            System.out.println("You have won the duel!");
            monstersInRoom.remove(monster.getName());
        } else {
            double damageReturned = monster.getAttack() - player.getDefense();
            player.setHealth(player.getHealth() - damageReturned);
            if (player.getHealth() <= 0) {
                System.out.println("You have died.");
                System.exit(0);
            }
        }
    }

    public static void attackWith(Monster monster, String input) {
        input = input.substring(12);
        boolean invalidInput = true;
        Item weapon = new Item();
        for (Item e: myItems) {
            if (input.equalsIgnoreCase(e.getName())) {
                weapon = e;
                invalidInput = false;
            }
        }
        if (invalidInput) {
            System.out.println("I do not have that item to attack with.");
            return;
        }
        double damage = player.getAttack() + weapon.getDamage() - monster.getDefense();
        monster.setHealth(monster.getHealth() - damage);
        if (monster.getHealth() <= 0) {
            System.out.println("You have won the duel!");
            monstersInRoom.remove(monster.getName());
        } else {
            double damageReturned = monster.getAttack() - player.getDefense();
            player.setHealth(player.getHealth() - damageReturned);
            if (player.getHealth() <= 0) {
                System.out.println("You have died.");
                System.exit(0);
            }
        }
    }

    public static void duelStatus() {
        return;
    }

    public static void list() {
        if(myItems == null || myItems.size() == 0) {
            System.out.println("You are not carrying any items" + "\n");
        } else {
            System.out.println("The items you are carrying are: ");
            for (Item e: myItems) {
                System.out.println(e.getName());
            }
            System.out.println();
        }
    }

    /**
     * drop an item into room and remove from my own items
     * @param input the item to drop into the current room
     */
    public static void drop(String input) {
        if(input.length() > 4) {
            input = input.substring(5);
            boolean invalidInput = true;
            for (int i = 0; i < myItems.size(); i++) {
                //making sure input is an option
                Item item = myItems.get(i);
                if (input.equals(item.getName())) {
                    invalidInput = false;
                    currentItems.add(item);
                    myItems.remove(item);
                    break;
                }
            }
            if (invalidInput) {
                System.out.println("I can't drop that item" + "\n");
            }
        } else {
            System.out.println("Please specify which item you would like to drop." + "\n");
        }
    }

    /**
     * takes item from current room and adds to my own items
     * @param input to take from room
     */
    public static void take(String input) {
        if(input.length() > 4) {
            input = input.substring(5);
            boolean invalidInput = true;

            if (monstersInRoom.size() > 0) {
                System.out.println("There are still monsters here, I can't take that." + "\n");
                return;
            }

            for (int i = 0; i < currentItems.size(); i++) {
                //making sure input is an option
                Item item = currentItems.get(i);
                if (input.contains(item.getName())) {
                    invalidInput = false;
                    System.out.println("hit");
                    myItems.add(item);
                    currentItems.remove(item);
                    break;
                }
            }
            if (invalidInput) {
                System.out.println("I can't take that item" + "\n");
            }
        } else {
            System.out.println("Please specify the item you would like to take." + "\n");
        }
    }

    /**
     * resets the current room to where the user chooses to go
     * @param input of where to move
     */
    public static void moveRooms(String input) {
        if(input.length() > 2) {
            input = input.substring(3);
            boolean invalidInput = true;

            if (monstersInRoom.size() > 0) {
                System.out.println("There are still monsters here, I can't move." + "\n");
                return;
            }

            for (int i = 0; i < currentRoom.getDirections().length; i++) {
                //making sure input is an option
                if (input.equals(currentRoom.getDirections()[i].getDirectionName().toLowerCase())) {
                    invalidInput = false;
                    String newRoom = currentRoom.getDirections()[i].getRoom();
                    for (int j = 0; j < rooms.length; j++) {
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
            if (invalidInput) {
                System.out.println("I can't go that direction" + "\n");
            }
        } else {
            System.out.println("Please specify the direction you would like to go." + "\n");
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
