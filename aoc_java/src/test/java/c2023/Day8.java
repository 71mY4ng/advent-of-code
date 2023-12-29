package c2023;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Day8 {

    @Test
    void test() throws IOException {

        var input = """
                LLR
                                
                AAA = (BBB, BBB)
                BBB = (AAA, ZZZ)
                ZZZ = (ZZZ, ZZZ)
                """;

        Path testResource = Paths.get("src", "test", "resources");
        List<String> lines = Files.readAllLines(testResource.resolve("adventofcode_2023_day8.input"));
//        List<String> lines = Arrays.stream(input.split("\n")).toList();


        String instructions = lines.get(0);
        char[] steps = instructions.toCharArray();

        Map<String, List<String>> mappings = new LinkedHashMap<>();
        lines.stream().skip(2).forEach(e -> {
            String[] split = e.split(" = ");
            String key = split[0];
            String[] pairSplit = split[1].replaceAll("[()]", "").split(", ");
            mappings.put(key, Arrays.stream(pairSplit).toList());
        });
        String nextKey = "AAA"; // initial

        do {
            for (char step : steps) {
                nextKey = getNextKey(nextKey, step, mappings);
                if ("ZZZ".equals(nextKey)) {
                    break;
                }
            }
        } while (!"ZZZ".equals(nextKey));

        System.out.println(count);
    }
    public static int count = 0;

    public static String getNextKey(String key, char step, Map<String, List<String>> mappings) {
        count++;
        List<String> pair = mappings.get(key);
        if (step == 'L') {
            return pair.get(0);
        }

        return pair.get(1);
    }
}
