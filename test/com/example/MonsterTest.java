package com.example;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MonsterTest {

    private static final String MONSTER_JSON = "{\n" +
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

    private static Monster monster;

    @Before
    public void setUp() throws Exception {
        Gson gson = new Gson();
        Layout layout = gson.fromJson(MONSTER_JSON,Layout.class);
        monster = layout.getMonsters()[0];
    }

    @Test
    public void getName() {
        assertEquals("CookieMonster", monster.getName());
    }

    @Test
    public void getAttack() {
        assertEquals(70.0, monster.getAttack(), 0.0000000001);
    }

    @Test
    public void getDefense() {
        assertEquals(100.0, monster.getDefense(), 0.0000000001);
    }

    @Test
    public void getHealth() {
        assertEquals(100.0, monster.getHealth(), 0.0000000001);
    }
}