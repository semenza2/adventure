package com.example;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DirectionTest {

    private static final String DIRECTION_JSON = "{\n" +
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

    private static Direction direction;

    @Before
    public void setUp() throws Exception {
        Gson gson = new Gson();
        direction = gson.fromJson(DIRECTION_JSON,Direction.class);
    }

    @Test
    public void getDirectionName() {
    }

    @Test
    public void getRoom() {
    }
}