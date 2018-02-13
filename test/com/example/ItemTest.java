package com.example;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ItemTest {

    private static final String ITEM_JSON = "{\n" +
            "  \"startingRoom\": \"MatthewsStreet\",\n" +
            "  \"endingRoom\": \"Siebel1314\",\n" +
            "    \"player\":\n" +
            "        {\n" +
            "            \"name\": \"Esarv\",\n" +
            "            \"items\" : [\n" +
            "\t\t{\n" +
            "\t\t\"name\" : \"pom-pom\",\n" +
            "\t\t \"damage\" :20.0\n" +
            "\t\t}, \n" +
            "\t\t{\n" +
            "\t\t\"name\": \"capacitor\", \n" +
            "\t\t\"damage\": 70.0\n" +
            "\t\t}\n" +
            "\t],\n" +
            "            \"attack\": 70.0,\n" +
            "            \"defense\": 40.0,\n" +
            "            \"health\": 100.0,\n" +
            "            \"level\": 2\n" +
            "        },\n" +
            "    \"monsters\": [            \n" +
            "        {\n" +
            "            \"name\" : \"CookieMonster\",\n" +
            "            \"attack\" : 70.0,\n" +
            "            \"defense\" : 100.0,\n" +
            "            \"health\" : 100.0\n" +
            "        },\n" +
            "        ],\n" +
            "  \"rooms\": [\n" +
            "    {\n" +
            "      \"name\": \"MatthewsStreet\",\n" +
            "      \"description\": \"You are on Matthews, outside the Siebel Center\",\n" +
            "      \"items\" : [\n" +
            "\t\t{\n" +
            "\t\t\"name\" : \"coin\",\n" +
            "\t\t \"damage\" :20.0\n" +
            "\t\t}\n" +
            "\t],\n" +
            "      \"directions\": [\n" +
            "        {\n" +
            "          \"directionName\": \"East\",\n" +
            "          \"room\": \"SiebelEntry\"\n" +
            "        }\n" +
            "      ],\n" +
            "        \"monstersInRoom\": [\"CookieMonster\",\"BigBird\"]\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    private static Item[] playerItems;
    private static Item[] roomItems;

    @Before
    public void setUp() throws Exception {
        Gson gson = new Gson();
        Layout layout = gson.fromJson(ITEM_JSON,Layout.class);
        roomItems = layout.getRooms()[0].getItems();
        playerItems = layout.getPlayer().getItems();
    }

    @Test
    public void getNameOfPlayerItems() {
        assertEquals("pom-pom", playerItems[0].getName());
    }

    @Test
    public void getNameOfRoomItems() {
        assertEquals("coin", roomItems[0].getName());
    }

    @Test
    public void getDamageOfPlayerItems() {
        assertEquals(20.0, playerItems[0].getDamage(), 0.000000001);
    }

    @Test
    public void getDamageOfRoomItems() {
        assertEquals(20.0, roomItems[0].getDamage(), 0.000000001);
    }
}