package com.example;
import com.google.gson.Gson;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.ArrayList;
import java.util.Arrays;
import java.net.MalformedURLException;
import java.util.Scanner;
import java.util.HashMap;

public class Adventure {

    private static Room currentRoom = new Room();
    private static ArrayList<Item> currentItems;
    private static ArrayList<String> monstersInRoom;
    private static ArrayList<Item> myItems;
    private static Layout layout;
    private static Player player = new Player();
    private static Room[] rooms;
    private static String originalInput;
    private static boolean hasDisengaged;
    private static boolean gameIsOver=false;

    private static final double MAX_HEALTH = 100.0;
    private static final double MAX_DEFENSE = 100.0;
    private static final double MAX_ATTACK = 100.0;

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
        if(monstersInRoom.size() == 0) {
            System.out.println("The directions you can move are: ");
            for (Direction e : currentRoom.getDirections()) {
                System.out.println(e.getDirectionName());
            }
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
            duelStatus(monster);
        } else if (input.equalsIgnoreCase("list")) {
            list();
        } else if (input.equalsIgnoreCase("playerinfo")) {
            getPlayerInfo();
        } else {
            System.out.println("I don't understand '" + originalInput + "'");
        }
    }

    /**
     * attacking a monster
     * @param monster the monster you are dueling
     */
    public static void attack(Monster monster) {
        double damage = player.getAttack() - monster.getDefense();
        //if player's attack is stronger than the monster's defense
        if (damage > 0) {
            monster.setHealth(monster.getHealth() - damage);
        }
        if (monster.getHealth() <= 0) {
            System.out.println("You have won the duel!");
            updateExperience(monster, player.getLevel());
            monstersInRoom.remove(monster.getName());
        } else {
            double damageReturned = monster.getAttack() - player.getDefense();
            //if monsters' attack is larger than player's defense
            if (damageReturned > 0) {
                player.setHealth(player.getHealth() - damageReturned);
            }
            if (player.getHealth() <= 0) {
                System.out.println("You have died.");
                System.exit(0);
            }
        }
    }

    /**
     * attatcking a monster with an item
     * @param monster the monster you are dueling
     * @param input contains the object you want to use
     */
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
        if (damage > 0) {
            monster.setHealth(monster.getHealth() - damage);
        }
        if (monster.getHealth() <= 0) {
            System.out.println("You have won the duel!");
            updateExperience(monster, player.getLevel());
            monstersInRoom.remove(monster.getName());
        } else {
            double damageReturned = monster.getAttack() - player.getDefense();
            if (damageReturned > 0) {
                player.setHealth(player.getHealth() - damageReturned);
            }
            if (player.getHealth() <= 0) {
                System.out.println("You have died.");
                System.exit(0);
            }
        }
    }

    /**
     * updates how much experience the player has after winning a battle
     * @param monster the monster you just defeated
     * @param level the current level you were on before the battle
     */
    public static void updateExperience(Monster monster, int level) {
        double experience = (((monster.getAttack() + monster.getDefense()) / 2) + monster.getHealth()) * 20;
        player.setExperience(player.getExperience() + experience);
        if (player.getExperience() > required(level)) {
            player.setLevel(level + 1);
            levelUp();
        }
    }

    /**
     * the required experinece before you can level up
     * @param level the current level you are on
     * @return the required experience to move past that level
     */
    public static double required(int level) {
        if (level == 0) {
            return 0.0;
        } else if (level == 1) {
            return 25.0;
        } else if (level == 2) {
            return 50.0;
        } else {
            return (required(level - 1) + required(level - 2)) * 1.1;
        }
    }

    /**
     * awards an increase in health, attack, and defense when you reach a new level
     */
    public static void levelUp() {
        if ((player.getAttack() * 1.5) > MAX_ATTACK) {
            player.setAttack(MAX_ATTACK);
        } else {
            player.setAttack(player.getAttack() * 1.5);
        }
        if ((player.getDefense() * 1.5) > MAX_DEFENSE) {
            player.setDefense(MAX_DEFENSE);
        } else {
            player.setDefense(player.getDefense() * 1.5);
        }
        if ((player.getHealth() * 1.3) > MAX_HEALTH) {
            player.setHealth(MAX_HEALTH);
        } else {
            player.setHealth(player.getHealth() * 1.3);
        }
    }

    /**
     * prints the monster and player's updated health status
     * @param monster the monster you are dueling
     */
    public static void duelStatus(Monster monster) {
        HashMap<Double,String> hm=new HashMap<Double,String>();
        hm.put(0.0,"__________");
        hm.put(10.0,"#_________");
        hm.put(20.0,"##________");
        hm.put(30.0, "###_______");
        hm.put(40.0, "####______");
        hm.put(50.0, "#####_____");
        hm.put(60.0, "######____");
        hm.put(70.0, "#######___");
        hm.put(80.0, "########__");
        hm.put(90.0, "#########_");
        hm.put(100.0, "##########");
        System.out.println("Player: " + hm.get(convert(player.getHealth())));
        System.out.println("Monster: " + hm.get(convert(player.getHealth())));
    }

    /**
     * converts health levels to a multiple of ten so can use hashmap
     * @param health the current health of player
     * @return the multiple of ten
     */
    public static double convert(double health) {
        if (health <= 0.0) {
            return 0.0;
        } else if (health > 0.0 && health <= 10.0) {
            return 10.0;
        } else if (health > 10.0 && health <= 20.0) {
            return 20.0;
        } else if (health > 20.0 && health <= 30.0) {
            return 30.0;
        } else if (health > 30.0 && health <= 40.0) {
            return 40.0;
        } else if (health > 40.0 && health <= 50.0) {
            return 50.0;
        } else if (health > 50.0 && health <= 60.0) {
            return 60.0;
        } else if (health > 60.0 && health <= 70.0) {
            return 70.0;
        } else if (health > 70.0 && health <= 80.0) {
            return 80.0;
        } else if (health > 80.0 && health <= 90.0) {
            return 90.0;
        } else {
            return 100.0;
        }
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
