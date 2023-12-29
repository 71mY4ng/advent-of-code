package c2023;

public class Day3 {

//    @Test
//    void test() throws IOException {
//        var input = """
//                467..114..
//                ...*......
//                ..35..633.
//                ......#...
//                617*......
//                .....+.58.
//                ..592.....
//                ......755.
//                ...$.*....
//                .664.598..
//                """;
//        String[] lines = input.split("\n");
//
//        Path testResource = Paths.get("src", "test", "resources");
////        List<String> lines = Files.readAllLines(testResource.resolve("adventofcode_2023_day3.input"));
//        char[][] grid = new char[lines.length][];
//
//        for (int i = 0; i < lines.length; i++) {
//            var line = lines[i];
//            grid[i] = line.toCharArray();
//        }
//
//        for (int x = 0; x < grid.length; x++) {
//            for (int y = 0; y < grid[x].length; y++) {
//                var digit = grid[x][y];
//                if (digit == '.') {
//                    continue;
//                }
//
//                if (!isNumberChar(digit)) {
//                }
//
//                for (int i = y; i < grid[x].length; i++) {
//                    grid[x][i]
//
//                }
//
//            }
//        }
//
//
//        int ans = 0;
//    }

    private static boolean isNumberChar(char digit) {
        return digit >= 48 && digit <= 57;
    }

}
