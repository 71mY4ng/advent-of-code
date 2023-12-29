package c2023;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day4 {

    @Test
    void test() throws IOException {
        var input = """
                Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
                Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
                Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
                Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
                Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
                Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
                """;
//        String[] lines = input.split("\n");

        int ans = 0;
        int ans2 = 0;
        Path testResource = Paths.get("src", "test", "resources");
        List<String> lines = Files.readAllLines(testResource.resolve("adventofcode_2023_day4.input"));
        for (String line : lines) {
            String[] game = line.split(":")[1].split("\\|");
            String[] winnings = game[0].trim().split(" ");
            String[] catches = game[1].trim().split(" ");

            Set<String> sWinnings = Arrays.stream(winnings).collect(Collectors.toSet());
            Set<String> sCatches = Arrays.stream(catches).collect(Collectors.toSet());
            sCatches.retainAll(sWinnings);
            sCatches.removeIf(String::isBlank);
            if (!sCatches.isEmpty()) {
                int resultVal = (int) Math.pow(2, sCatches.size() - 1);
                System.out.println(sCatches + ", " + resultVal);
                ans += resultVal;
            }
        }


        int[] scratchcards = new int[lines.size()];

        for (String line : lines) {
            var split = line.split(":");
            int gameId = Integer.parseInt(split[0].substring(4).trim());
            String[] game = split[1].split("\\|");
            String[] winnings = game[0].trim().split(" ");
            String[] catches = game[1].trim().split(" ");

            Set<String> sWinnings = Arrays.stream(winnings).collect(Collectors.toSet());
            Set<String> sCatches = Arrays.stream(catches).collect(Collectors.toSet());
            sCatches.retainAll(sWinnings);
            sCatches.removeIf(String::isBlank);
            int matchingNum = sCatches.size();

            scratchcards[gameId - 1] += 1;
            if (!sCatches.isEmpty()) {
                for (int i = gameId; i < (gameId + matchingNum); i++) {
                    scratchcards[i] += scratchcards[gameId - 1];
                }
            }
        }
        System.out.println(ans);
        System.out.println(Arrays.stream(scratchcards).sum());
    }

}
