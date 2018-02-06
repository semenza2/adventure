package com.example;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LayoutTest {

    private static final String LAYOUT_JSON = "{\n" +
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

    private static Layout layout;

    @Before
    public void setUp() throws Exception {
        Gson gson = new Gson();
        layout = gson.fromJson(LAYOUT_JSON,Layout.class);
    }

    @Test
    public void getStartingRoom() {
        assertEquals("MatthewsStreet",layout.getStartingRoom());
    }

    @Test
    public void getEndingRoom() {
        assertEquals("Siebel1314",layout.getEndingRoom());
    }

    @Test
    public void getRooms() {
    }
}