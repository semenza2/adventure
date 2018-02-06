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
            "  \"rooms\": [\n" +
            "    {\n" +
            "      \"name\": \"MatthewsStreet\",\n" +
            "      \"description\": \"You are on Matthews, outside the Siebel Center\",\n" +
            "      \"items\": [\"coin\"],\n" +
            "      \"directions\": [\n" +
            "        {\n" +
            "          \"directionName\": \"East\",\n" +
            "          \"room\": \"SiebelEntry\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "  ]\n" +
            "}";

    private Direction[] directionsArray = {new Direction("East", "Siebel Entry")};
    private String[] itemsArray = {"coin"};

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
        assertTrue(Arrays.equals(itemsArray, rooms[0].getItems()));
    }
}