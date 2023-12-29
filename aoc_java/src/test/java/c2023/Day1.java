package c2023;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Day1 {

    public static final List<String> NUMBER_TXTS = Arrays.asList("one", "two", "three", "four", "five", "six", "seven", "eight", "nine");

    @Test
    void test1() throws IOException {
        var input = """
                two1nine
                eightwothree
                abcone2threexyz
                xtwone3four
                4nineeightseven2
                zoneight234
                7pqrstsixteen
                """;
        Path testResource = Paths.get("src", "test", "resources");
        List<String> lines = Files.readAllLines(testResource.resolve("adventofcode_2023_day1.input"));
        String[] split = input.split("\n");

        int ans = 0;
        for (String l : lines) {
            char[] chars = l.toCharArray();
            int numFirstFound = traverse(chars);
            Hit numFirstHit = numFirstFound >= 0 ? Hit.of(numFirstFound, chars[numFirstFound]) : null;
            int numLastFound = reverse(chars);
            Hit numLastHit = numLastFound >= 0 ? Hit.of(numLastFound, chars[numLastFound]) : null;

            List<Hit> hits = NUMBER_TXTS.stream()
                    .filter(l::contains)
                    .map(alt -> new HashSet<>(Arrays.asList(Hit.of(l.indexOf(alt), alt), Hit.of(l.lastIndexOf(alt), alt))))
                    .flatMap(Collection::stream)
                    .sorted()
                    .toList();
            Hit wordFirstHit = hits.isEmpty() ? null : hits.get(0);
            Hit wordLastHit = hits.isEmpty() ? null : hits.get(hits.size() - 1);

            Hit firstHit = Objects.compare(numFirstHit, wordFirstHit, Comparator.nullsLast(Comparator.comparingInt(Hit::index)).reversed()) >= 0 ? numFirstHit : wordFirstHit;
            Hit lastHit = Objects.compare(numLastHit, wordLastHit, Comparator.nullsFirst(Comparator.comparingInt(Hit::index))) >= 0 ? numLastHit : wordLastHit;
            String first = firstHit.evalStr;
            String last = lastHit.evalStr;

            System.out.println(l + "; first: " +first+ ", last: " + last);

            ans += Integer.parseInt(first + last);
        }
        System.out.println(ans);
    }
    record Hit(int index, String evalStr) implements Comparable<Hit> {
        public static Hit of(int index, char c) {
            String translated = Character.toString(c);
            return new Hit(index, translated);
        }
        public static Hit of(int index, String word) {
            String translated = String.valueOf(NUMBER_TXTS.indexOf(word) + 1);
            return new Hit(index, translated);
        }

        @Override
        public int compareTo(Hit o) {
            return Integer.compare(index, o.index);
        }
    }

    static int traverse(char[] chars) {
        for (int i = 0; i < chars.length; i++) {
            int c = chars[i];
            if (c >= 48 && c <= 57) {
                return i;
            }
        }
        return -1;
    }

    static int reverse(char[] chars) {
        for (int i = chars.length - 1; i >= 0; i--) {
            var c = chars[i];
            if (c >= 48 && c <= 57) {
                return i;
            }
        }
        return -1;
    }

    @Test
    void test2() {

//        Set<Hit> hits = Set.of(Hit.of(1, '1'), Hit.of(1, '1'));
//        System.out.println(hits);
    }

}
