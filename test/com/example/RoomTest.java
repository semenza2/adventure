package com.example;

import com.google.gson.Gson;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RoomTest {

    private static final String ROOM_JSON = "{\n" +
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

    private Direction[] directionsArray = {new Direction("East", "Siebel Entry")};
    private String[] monsters = {"CookieMonster", "BigBird"};
    private static Room[] rooms;

    @Before
    public void setUp() throws Exception {
        Gson gson = new Gson();
        Layout layout = gson.fromJson(ROOM_JSON,Layout.class);
        rooms =layout.getRooms();
    }

    @Test
    public void getName() {
        assertEquals("MatthewsStreet",rooms[0].getName());
    }

    @Test
    public void getDescription() {
        assertEquals("You are on Matthews, outside the Siebel Center",rooms[0].getDescription());
    }

    @Test
    public void getDirections() {
        assertTrue(directionsArray.length == rooms[0].getDirections().length);
    }

    @Test
    public void getItems() {
        assertTrue(rooms[0].getItems().length == 1);
    }

    @Test
    public void getMonstersInRoom() {
        assertArrayEquals(monsters, rooms[0].getMonstersInRoom());
    }
}