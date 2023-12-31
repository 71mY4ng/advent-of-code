package c2023;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class Day10 {

    public static final Map<Character, Integer> asciiArtMapping = Map.of(
            '|', 0x2551,
            'F', 0x2554,
            'L', 0x255A,
            '7',0x2557,
            'J',0x255D,
            '-',0x2550,
            '.', 0x00B0,
            'S', 0x058D
    );

    public static class ExtPoint {
        private Point original;
        private boolean isLoopPath;
        private boolean inside;

        public ExtPoint(Point original, boolean isLoopPath) {
            this.original = original;
            this.isLoopPath = isLoopPath;
        }

        public void setInside(boolean inside) {
            this.inside = inside;
        }

        public Point getOriginal() {
            return original;
        }

        public char getPrettyChar() {
            if (isInside()) {
                return 'I';
            }
            var p =  Optional.ofNullable(getOriginal()).map(Point::c).orElse('.');
            return (char) asciiArtMapping.get(p).intValue();
        }

        public boolean isLoopPath() {
            return isLoopPath;
        }

        public boolean isInside() {
            return inside;
        }

        public static ExtPoint of(Point point, boolean isLoop) {
            return new ExtPoint(point, isLoop);
        }
    }
    public static class Grid {
        private int xDimension;
        private int yDimension;
        private Point[][] grids = new Point[xDimension][yDimension];
        private Point startingPoint;
        private List<List<Point>> loops = new ArrayList<>();

        public Grid(int dimension) {
            this.xDimension = dimension;
            this.yDimension = dimension;
        }
        public Grid(int xDimension, int yDimension) {
            this.xDimension = xDimension;
            this.yDimension = yDimension;
        }

        public Point[][] getGrids() {
            return grids;
        }

        public int getxDimension() {
            return xDimension;
        }

        public void setxDimension(int xDimension) {
            this.xDimension = xDimension;
        }

        public int getyDimension() {
            return yDimension;
        }

        public void setyDimension(int yDimension) {
            this.yDimension = yDimension;
        }

        public void setGrids(Point[][] grids) {
            this.grids = grids;
        }

        public Point getStartingPoint() {
            return startingPoint;
        }

        public void setStartingPoint(Point startingPoint) {
            this.startingPoint = startingPoint;
        }

        public List<List<Point>> getLoops() {
            return loops;
        }

        public void setLoops(List<List<Point>> loops) {
            this.loops = loops;
        }

        public List<List<Point>> findLoops() {
            Point startingPoint = getStartingPoint();
            int x = startingPoint.x;
            int y = startingPoint.y;

            List<int[]> firstSteps = List.of(
                    new int[]{x + 1, y},
                    new int[]{x - 1, y},
                    new int[]{x, y + 1},
                    new int[]{x, y - 1}
            );
            for (int[] pair : firstSteps) {
                LinkedList<Point> traces = new LinkedList<>();
                int loopCount = findLoopCount(startingPoint, pair[0], pair[1], traces);
                if (loopCount > 0) {
                    loops.add(traces);
                }
            }

            return loops;
        }

        public int findLoopCount(Point last, int x, int y, LinkedList<Point> traces) {
            while (true) {
                if (x < 0 || y < 0 || x >= xDimension|| y >= yDimension) {
                    return 0;
                }
                Point point = grids[x][y];
                if (point == null) {
                    return 0;
                }

                if (last != point && point == startingPoint) {
//                    System.out.println("Loop! " + traces);
                    return traces.size();
                }
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
                if (nx < 0 || ny < 0 || nx >= xDimension || ny >= yDimension) {
                    return point;
                }

                if (last != grids[nx][ny] && point.isConnect(grids[nx][ny], direction.predicate)) {
                    return grids[nx][ny];
                }
            }
            return point;
        }

        static Predicate<Point> rightExpected = point -> 'J' == point.c || '-' == point.c || '7' == point.c;
        static Predicate<Point> leftExpected = point -> '-' == point.c || 'L' == point.c || 'F' == point.c;
        static Predicate<Point> upExpected = point -> 'F' == point.c || '|' == point.c || '7' == point.c;
        static Predicate<Point> downExpected = point -> 'J' == point.c || '|' == point.c || 'L' == point.c;
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

    public Grid initGrid(String[] lines) {
        int xDim = lines.length;
        int yDim = lines[0].toCharArray().length;

        Grid grid = new Grid(xDim, yDim);
        Point[][] grids = new Point[xDim][yDim];

        for (int i = 0; i < xDim; i++) {
            char[] split = lines[i].toCharArray();
            for (int j = 0; j < yDim; j++) {
                char thisC = split[j];
                grids[i][j] = thisC == '.' ? null : new Point(i, j, thisC, false);

                if (thisC == 'S') {
                    grid.setStartingPoint(grids[i][j]);
                }
            }
        }
        grid.setGrids(grids);

        if (grid.getStartingPoint() == null) {
            throw new RuntimeException();
        }
        return grid;
    }

    @Test
    void part1() throws IOException {

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

        Grid grid = initGrid(lines);
        List<List<Point>> loops = grid.findLoops();
        Optional<Integer> maxLoopCnt = loops.stream()
                .map(l -> (int) Math.ceil((double) l.size() / 2))
                .max(Integer::compare);
        System.out.println(maxLoopCnt);
    }


    public record Point(int x, int y, char c, boolean isLoopPipe) {
        boolean isConnect(Point point, Predicate<Point> predicate) {
            return point != null && (point.c == 'S' || predicate.test(point));
        }
    }

    public record Direction(Function<Integer, Integer> x, Function<Integer, Integer> y, Predicate<Point> predicate) {
    }

    static char guessStartingPointDirection(Point first, Point last, Point sp) {
        if ( first.x == last.x ) {
            return '-';
        }
        if ( first.y == last.y ) {
            return '|';
        }
        // calc vector for direction, the coordinate is upside down
        int vx = first.x + last.x - sp.x * 2;
        int vy = first.y + last.y - sp.y * 2;
//        System.out.printf("vx: %d, vy: %d \n", vx, vy);
        if (vx < 0 && vy < 0) {
            return 'J';
        }
        if (vx < 0 && vy > 0) {
            return 'L';
        }
        if (vx > 0 && vy < 0) {
            return '7';
        }
        if (vx > 0 && vy > 0) {
            return 'F';
        }

        return 'S';
    }

    @Test
    void testGuessSp() {
        Point start = new Point(-1, 0, (char) 0, false);
        Point end = new Point(0, 1, (char) 0, false);
        Point sp = new Point(0, 0, (char) 0, false);

        char c = guessStartingPointDirection(start, end, sp);
        char c1 = guessStartingPointDirection(end, start, sp);
        Assertions.assertEquals('L', c);
        Assertions.assertEquals(c, c1);

        Point start2 = new Point(-1, 0, (char) 0, false);
        Point end2 = new Point(0, -1, (char) 0, false);

        guessStartingPointDirection(start2, end2, sp);
        guessStartingPointDirection(end2, start2, sp);

        Point start3 = new Point(1, 0, (char) 0, false);
        Point end3 = new Point(0, -1, (char) 0, false);

        guessStartingPointDirection(start3, end3, sp);
        guessStartingPointDirection(end3, start3, sp);

        Point start4 = new Point(0, 1, (char) 0, false);
        Point end4 = new Point(1, 0, (char) 0, false);

        guessStartingPointDirection(start4, end4, sp);
        guessStartingPointDirection(end4, start4, sp);
    }

    @Test
    void part2() throws IOException {

        String input = """
                .F----7F7F7F7F-7....
                .|F--7||||||||FJ....
                .||.FJ||||||||L7....
                FJL7L7LJLJ||LJ.L-7..
                L--J.L7...LJS7F-7L7.
                ....F-J..F7FJ|L7L7L7
                ....L7.F7||L7|.L7L7|
                .....|FJLJ|FJ|F7|.LJ
                ....FJL-7.||.||||...
                ....L---J.LJ.LJLJ...
                """;
        Path testResource = Paths.get("src", "test", "resources");
        List<String> allLines = Files.readAllLines(testResource.resolve("adventofcode_2023_day10.input"));
        String[] lines = allLines.toArray(new String[0]);
//        String[] lines = input.split("\n");

        Grid grid = initGrid(lines);
        List<List<Point>> loops = grid.findLoops();
        Point[][] grids = grid.getGrids();
        Point startingPoint = grid.getStartingPoint();

        int xDim = grid.getxDimension();
        int yDim = grid.getyDimension();
        ExtPoint[][] newGrids = new ExtPoint[xDim][yDim];

        loops.sort(Comparator.comparingInt(List::size));
        List<Point> points = loops.get(loops.size() - 1);
        Point first = points.get(0);
        Point last = points.get(points.size() -1);
        char spGuess = guessStartingPointDirection(first, last, startingPoint);
        Point sp = new Point(startingPoint.x, startingPoint.y, spGuess, true);

        List<Point> farestLoop = new ArrayList<>(List.of(sp));
        farestLoop.addAll(points);

        int twiceArea = 0;
        for (int i = 0; i < farestLoop.size() - 1; i++) {
            Point cur = farestLoop.get(i);
            Point next = farestLoop.get(i + 1);
            twiceArea += cur.x * next.y;
        }
        for (int i = 0; i < farestLoop.size() - 1; i++) {
            Point cur = farestLoop.get(i);
            Point next = farestLoop.get(i + 1);
            twiceArea -= cur.y * next.x;
        }

        System.out.println(twiceArea);
        int pickPointCnt = ( Math.abs(twiceArea) + 2 - (farestLoop.size() -1) )/ 2;
        System.out.println("pickPointCnt = " + pickPointCnt);

        for (Point point : farestLoop) {
            newGrids[point.x][point.y] = ExtPoint.of(point, true);
        }
        newGrids[sp.x][sp.y] = ExtPoint.of(sp, true);

        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                if (newGrids[i][j] == null) {
                    newGrids[i][j] = ExtPoint.of(null, false);
                }
            }
        }

        int ans = 0;
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                ExtPoint point = newGrids[i][j];
                boolean isNotPipe = point.getOriginal() == null;
                if (isNotPipe) {
                    ExtPoint[] slice = Arrays.copyOfRange(newGrids[i], j + 1, yDim);
                    long pipeCnt = Stream.of(slice)
                            .filter(ExtPoint::isLoopPath)
                            .map(p -> p.getOriginal().c)
                            .filter(c -> c == '|').count();
                    long downwardCnt = Stream.of(slice)
                            .filter(ExtPoint::isLoopPath)
                            .map(p -> p.getOriginal().c)
                            .filter(c -> c == 'L' || c == '7' ).count() / 2;
                    long upwardCnt = Stream.of(slice)
                            .filter(ExtPoint::isLoopPath)
                            .map(p -> p.getOriginal().c)
                            .filter(c -> c == 'F' || c == 'J').count() / 2;

                    if ((pipeCnt - upwardCnt + downwardCnt) % 2 != 0) {
                        if (newGrids[i][j] == null) {
                            newGrids[i][j] = ExtPoint.of(grids[i][j], false);
                        }
                        newGrids[i][j].setInside(true);
                        ans ++;
                    }
                }

            }
        }

        for (ExtPoint[] row : newGrids) {
//            System.out.println(Arrays.toString(row));
            System.out.println(Arrays.stream(row).map(ExtPoint::getPrettyChar).collect(Collector.of(StringBuilder::new,
                    StringBuilder::append,
                    StringBuilder::append,
                    StringBuilder::toString)));
//            System.out.println(Arrays.stream(row).map(ExtPoint::getPrettyChar).collect(Collectors.toList()));
        }
        System.out.println("insides = " + ans);

    }

    @Test
    void testUnicode() {
        System.out.println(Character.toString(0x231E)); // bottom left
        System.out.println(Character.toString(0x23CC)); // bottom left 2
        System.out.println(Character.toString(0x231F)); // bottom right
        System.out.println(Character.toString(0x231D)); // top right
        System.out.println(Character.toString(0x231C)); // top left

        System.out.printf("""
                                %c %c %c
                                %c %c %c
                                %c %c %c
                                %n""",
                0x2554, 0x2550, 0x2557,
                0x2551, '*', 0x2551,
                0x255A, 0x2550, 0x255D
        );

    }

}
