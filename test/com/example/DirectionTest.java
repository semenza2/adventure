package com.example;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DirectionTest {

    private static final String DIRECTION_JSON = "{\n" +
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

    private static Direction[] directions;

    @Before
    public void setUp() throws Exception {
        Gson gson = new Gson();
        Layout layout = gson.fromJson(DIRECTION_JSON,Layout.class);
        directions =layout.getRooms()[0].getDirections();
    }

    @Test
    public void getDirectionName() {
        assertEquals("East", directions[0].getDirectionName());
    }

    @Test
    public void getRoom() {
        assertEquals("SiebelEntry", directions[0].getRoom());
    }
}