package c2023;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day2 {
    public static final Map<String, Integer> limits = Map.of(
            "red", 12,
            "green", 13,
            "blue", 14
    );

    @Test
    void test() throws IOException {
        var input = """
                Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
                Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
                Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
                Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
                Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
                """;
//        String[] lines = input.split("\n");

        Path testResource = Paths.get("src", "test", "resources");
        List<String> lines = Files.readAllLines(testResource.resolve("adventofcode_2023_day2.input"));

        int ansP1 = 0;
        int ansP2 = 0;

        for (String line : lines) {
            String[] cols = line.split(":");
            String gameId = cols[0].substring(4).trim();
            String[] reveals = cols[1].split(";");

            ansP1 += processGame(gameId, reveals);
            ansP2 += processGameP2(gameId, reveals);
        }

        System.out.println(ansP1);
        System.out.println(ansP2);
    }

    int processGame(String gameId, String[] reveals) {
        for (String reveal : reveals) {
            String[] cubes = reveal.split(",");
            for (String cube : cubes) {
                String[] split = cube.trim().split(" ");
                int cnt = Integer.parseInt(split[0]);
                String color = split[1];
//                System.out.println("game: " + gameId + ", cnt: " + cnt + ", color: " + color);

                if (cnt > limits.get(color)) {
                    return 0;
                }
            }
        }
        return Integer.parseInt(gameId);
    }

    int processGameP2(String gameId, String[] reveals) {
        Map<String, Integer> countBoard = new HashMap<>(Map.of(
                "red", 1,
                "green", 1,
                "blue", 1
        ));

        for (String reveal : reveals) {
            String[] cubes = reveal.split(",");
            for (String cube : cubes) {
                String[] split = cube.trim().split(" ");
                int cnt = Integer.parseInt(split[0]);
                String color = split[1];
                System.out.println("game: " + gameId + ", cnt: " + cnt + ", color: " + color);

                countBoard.put(color, Math.max(countBoard.get(color), cnt));
            }
        }

        return countBoard.get("red") * countBoard.get("green") * countBoard.get("blue");
    }
}
