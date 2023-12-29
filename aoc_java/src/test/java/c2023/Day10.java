package c2023;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Day10 {
    public static final int DIMENSION = 140;
//    char[][] grids = new char[DIMENSION][];
    public static Point[][] grids = new Point[DIMENSION][DIMENSION];
    Point startingPoint = null;

    @Test
    void test() throws IOException {

        var input = """
                ..F7.
                .FJ|.
                SJ.L7
                |F--J
                LJ...
                """;

        Path testResource = Paths.get("src", "test", "resources");
        List<String> allLines = Files.readAllLines(testResource.resolve("adventofcode_2023_day10.input"));
        String[] lines = allLines.toArray(new String[0]);
//
//        String[] lines = input.split("\n");
        for (int i = 0; i < lines.length; i++) {
            char[] split = lines[i].toCharArray();
            for (int j = 0; j < split.length; j++) {
                char thisC = split[j];
                grids[i][j] = thisC == '.' ? null : new Point(i, j, thisC);

                if (thisC == 'S') {
                    startingPoint = grids[i][j];
                }
            }
        }
        if (startingPoint == null) {
            throw new RuntimeException();
        }
//        System.out.println(Arrays.deepToString(grids));
//        System.out.println(startingPoint);

        int x = startingPoint.x;
        int y = startingPoint.y;

        int up = findLoopCount(startingPoint, x + 1, y, new LinkedList<>());
        int down = findLoopCount(startingPoint, x - 1, y, new LinkedList<>());
        int right = findLoopCount(startingPoint, x, y + 1, new LinkedList<>());
        int left = findLoopCount(startingPoint, x, y - 1, new LinkedList<>());

        System.out.println(Stream.of(up, down, right, left).max(Integer::compare));
    }


    public int findLoopCount(Point last, int x, int y, LinkedList<Point> traces) {
        while (true) {
            if (x < 0 || y < 0 || x >= DIMENSION || y >= DIMENSION) {
                return 0;
            }
            Point point = grids[x][y];
            if (point == null) {
                return 0;
            }

            if (last != point && point == startingPoint) {
                System.out.println("Loop! " + traces);
                int ans = (int) Math.ceil((double) traces.size() / 2);
                System.out.println();
//                System.out.printf("""
//                                %c %c %c
//                                %c %c %c
//                                %c %c %c
//                                %n""",
//                        grids[28][71].c, grids[28][72].c, grids[28][73].c,
//                        grids[29][71].c, grids[29][72].c, grids[29][73].c,
//                        grids[30][71].c, grids[30][72].c, grids[30][73].c
//                );
                return ans;
            }
            //
            Point next = findNext(point, last);
            traces.addLast(point);
//            System.out.println(traces);
            last = point;
            x = next.x;
            y = next.y;
        }
    }

    public Point findNext(Point point, Point last) {
        List<Direction> directions = mapping.get(point.c);
        for (Direction direction : directions) {
            Integer nx = direction.x.apply(point.x);
            Integer ny = direction.y.apply(point.y);
            if (nx < 0 || ny < 0 || nx >= DIMENSION || ny >= DIMENSION) {
                return point;
            }

            if (last != grids[nx][ny] && point.isConnect(nx, ny, direction.predicate)) {
                return grids[nx][ny];
            }
        }
        return point;
    }

    static Predicate<Point> rightExpected = point -> 'J' == point.c || '-' == point.c || '7' == point.c;
    static Predicate<Point> leftExpected = point -> '-' == point.c || 'L' == point.c || 'F' == point.c;
    static Predicate<Point> upExpected = point -> 'F' == point.c || '|' == point.c || '7' == point.c;
    static Predicate<Point> downExpected = point -> 'J' == point.c || '|' == point.c || 'L' == point.c;

    public record Point(int x, int y, char c) {
        static Point of(int x, int y, char c) {
            return new Point(x, y, c);
        }

        boolean isConnect(int x, int y, Predicate<Point> predicate) {
            return isConnect(grids[x][y], predicate);
        }

        boolean isConnect(Point point, Predicate<Point> predicate) {
            return point != null && (point.c == 'S' || predicate.test(point));
        }
    }

    public record Direction(Function<Integer, Integer> x, Function<Integer, Integer> y, Predicate<Point> predicate) {
    }

    Map<Character, List<Direction>> mapping =
            Map.of(
                    '|', List.of(
                            new Direction(x -> x + 1, y -> y, downExpected),
                            new Direction(x -> x - 1, y -> y, upExpected)
                    ),
                    'F', List.of(
                            new Direction(x -> x, y -> y + 1, rightExpected),
                            new Direction(x -> x + 1, y -> y, downExpected)
                    ),
                    'L', List.of(
                            new Direction(x -> x, y -> y + 1, rightExpected),
                            new Direction(x -> x - 1, y -> y, upExpected)
                    ),
                    '7', List.of(
                            new Direction(x -> x, y -> y - 1, leftExpected),
                            new Direction(x -> x + 1, y -> y, downExpected)
                    ),
                    'J', List.of(
                            new Direction(x -> x, y -> y - 1, leftExpected),
                            new Direction(x -> x - 1, y -> y, upExpected)
                    ),
                    '-', List.of(
                            new Direction(x -> x, y -> y + 1, rightExpected),
                            new Direction(x -> x, y -> y - 1, leftExpected)
                    )
            );

}
