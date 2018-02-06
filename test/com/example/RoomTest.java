package com.example;

import com.google.gson.Gson;
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

    private static Room room;

    @Before
    public void setUp() throws Exception {
        Gson gson = new Gson();
        room = gson.fromJson(ROOM_JSON,Room.class);
    }

    @Test
    public void getName() {
        assertEquals("MatthewsStreet",room.getName());
    }

    @Test
    public void getDescription() {
        assertEquals("You are on Matthews, outside the Siebel Center",room.getDescription());
    }

    @Test
    public void getDirections() {
    }

    @Test
    public void getItems() {
    }
}