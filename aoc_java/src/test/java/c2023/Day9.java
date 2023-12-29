package c2023;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Day9 {

    @Test
    void test() throws IOException {

        var input = """
                0 3 6 9 12 15
                1 3 6 10 15 21
                10 13 16 21 30 45
                """;

        Path testResource = Paths.get("src", "test", "resources");
        List<String> lines = Files.readAllLines(testResource.resolve("adventofcode_2023_day9.input"));
//        List<String> lines = Arrays.stream(input.split("\n")).toList();

        int ans = 0;
        for (String line : lines) {
            LinkedList<Integer> seq = Arrays.stream(line.split(" "))
                    .map(Integer::parseInt)
                    .collect(Collectors.toCollection(LinkedList::new));

            List<Integer> tracks = new ArrayList<>();

            while (!seq.isEmpty()) {
                tracks.add(seq.peekLast());

                int size = seq.size();
                for (int i = 0; i < size - 1; i++) {
                    Integer cur = seq.pollLast();
                    Integer next = i == size - 2 ? seq.pollLast() : seq.peekLast();
                    int subtract = cur - next;
                    seq.addFirst(subtract);
//                    System.out.println("i: "+ i + ", " + cur + " - " + next + " = " + subtract + "; " + seq);
                }

                if (seq.stream().allMatch(e -> e == 0)) {
                    break;
                }
            }
            Integer sum = tracks.stream().reduce(Integer::sum).get();
//            System.out.println(sum);
            ans += sum;
        }
        System.out.println(ans);
    }
}
