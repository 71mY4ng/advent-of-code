package c2023;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day6 {

    @Test
    void test() throws IOException {
//        var input = """
//                Time:      7  15   30
//                Distance:  9  40  200
//                """;
        var input2 = """
Time:        58819676
Distance:   434104122191218
                """;


        Path testResource = Paths.get("src", "test", "resources");
//        List<String> lines = Files.readAllLines(testResource.resolve("adventofcode_2023_day6.input"));
        List<String> lines = Arrays.stream(input2.split("\n")).toList();

        String[] raceTimes = initRecords(lines.get(0));//lines[0]);
        String[] raceDistances = initRecords(lines.get(1));

        List<RaceResult> raceResults = new ArrayList<>();
        List<Long> wayAns = new ArrayList<>();
        for (int j = 0; j < raceTimes.length; j++) {
            raceResults.add(RaceResult.of(raceTimes[j], raceDistances[j]));
        }
        // btnSec * (raceTime - btnSec) > distance

        for (RaceResult raceResult : raceResults) {
            long i = getFirstWin(raceResult);
            wayAns.add(i);
        }
        Long ans = wayAns.stream().reduce(1L, (a, b) -> a * b);
        System.out.println(ans);
    }

    private static long getFirstWin(RaceResult raceResult) {
        for (int i = 1; i < raceResult.time; i++) {
            var guessDistance = i * (raceResult.time - i);
            if (guessDistance > raceResult.distance) {
                System.out.printf("race distance: %d, race time: %d; btnSec: %d, guessDist: %d%n", raceResult.distance, raceResult.time, i, guessDistance);
                return raceResult.time - (2L * i) + 1;
            }
        }
        return 1;
    }

    String[] initRecords(String line) {
        return line.split(":")[1].trim().split("\\s+");
    }

    record RaceResult (long time, long distance) {

        public static RaceResult of (String time, String distance) {
            return new RaceResult(Long.parseLong(time), Long.parseLong(distance));
        }
    }

}
