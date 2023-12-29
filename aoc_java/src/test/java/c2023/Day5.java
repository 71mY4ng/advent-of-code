package c2023;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day5 {

    @Test
    void test() throws IOException {
        var input = """
                seeds: 79 14 55 13

                seed-to-soil map:
                50 98 2
                52 50 48

                soil-to-fertilizer map:
                0 15 37
                37 52 2
                39 0 15

                fertilizer-to-water map:
                49 53 8
                0 11 42
                42 0 7
                57 7 4

                water-to-light map:
                88 18 7
                18 25 70

                light-to-temperature map:
                45 77 23
                81 45 19
                68 64 13

                temperature-to-humidity map:
                0 69 1
                1 0 69

                humidity-to-location map:
                60 56 37
                56 93 4
                """;

        Path testResource = Paths.get("src", "test", "resources");
//        String input = Files.readString(testResource.resolve("adventofcode_2023_day5.input"));
        String[] split = input.split("\n\n"); //input.split("\r\n\r\n");
        String[] seeds = split[0].split(":")[1].trim().split(" ");

        var seedToSoils = processMappings(split[1].split("\n"));
        var soilToFertilizer = processMappings(split[2].split("\n"));
        var fertilizerToWater = processMappings(split[3].split("\n"));
        var waterToLight = processMappings(split[4].split("\n"));
        var lightToTemperature = processMappings(split[5].split("\n"));
        var temperatureToHumidity = processMappings(split[6].split("\n"));
        var humidityToLocation = processMappings(split[7].split("\n"));

        List<Long> locations = new ArrayList<>(seeds.length);

        for (String seed : seeds) {
            long iSeed = Long.parseLong(seed);

            long soils = getMappingDest(seedToSoils, iSeed);
            long fertilizer = getMappingDest(soilToFertilizer, soils);
            long water = getMappingDest(fertilizerToWater, fertilizer);
            long light = getMappingDest(waterToLight, water);
            long temperature = getMappingDest(lightToTemperature, light);
            long humidity = getMappingDest(temperatureToHumidity, temperature);
            long location = getMappingDest(humidityToLocation, humidity);

//            System.out.println(String.format("Seed %d, soil %d, fertilizer %d, water %d, light %d, temperature %d, humidity %d, location %d.", iSeed, destSoils, destFertilizer, water, light, temperature, humidity, location));
            locations.add(location);
        }
        locations.sort(Long::compareTo);
        System.out.println(locations.get(0));

    }

    public long getMappingDest(List<Mapping> seedToSoils, long iSeed) {
        for (Mapping m : seedToSoils) {
            if (iSeed >= m.srcRngStart && iSeed <= m.srcRngEnd) {
                return (iSeed - m.srcRngStart) + m.destRngStart;
            }
        }
        return iSeed;
    }

    static List<Mapping> processMappings(String[] lines) {
        return Arrays.stream(lines)
                .skip(1)
                .map(String::trim)
                .map(s -> s.split(" "))
                .map(each -> Mapping.of(each[0], each[1], each[2]))
                .toList();
    }

    public record Mapping (long destRngStart, long destRngEnd, long srcRngStart, long srcRngEnd, long length) {

        public static Mapping of(String destRngStart, String srcRngStart, String length) {
            long len = Long.parseLong(length);
            long lDestRngStart = Long.parseLong(destRngStart);
            long lSrcRngStart = Long.parseLong(srcRngStart);
            return new Mapping(lDestRngStart, lDestRngStart + len - 1L, lSrcRngStart, lSrcRngStart + len - 1L, len);
        }

    }

}
