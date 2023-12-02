import java.awt.*;
import java.text.MessageFormat;
import java.util.List;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day14 {

    public static void main(String[] args) {

        FileReader<Wall> fileReader = new FileReader<>("day14",
                (index, line) -> lineToWall(line));
        List<Wall> walls = fileReader.readFile();
        Set<Point> wallPoints = new HashSet<>();
        walls.forEach(wall -> {
            Iterator<Point> pointIterator = wall.pinPoints.iterator();
            Point current = pointIterator.next();
            while (pointIterator.hasNext()) {
                Point next = pointIterator.next();
                Set<Point> result = getWallPoints(current, next);
                wallPoints.addAll(result);
                current = next;
            }
        });
        System.out.println("wallPoints = " + wallPoints);
        int minX = wallPoints.stream().map(point -> point.x).min(Integer::compare).orElseThrow();
        int maxX = wallPoints.stream().map(point -> point.x).max(Integer::compare).orElseThrow();
        int minY = wallPoints.stream().map(point -> point.y).min(Integer::compare).orElseThrow();
        int maxY = wallPoints.stream().map(point -> point.y).max(Integer::compare).orElseThrow();

        int caveHeight = maxY + 3;
        int caveWidth = caveHeight * 2; // make cave square

        int pouringX = caveHeight;

        int xShift = pouringX - 500;
        wallPoints.forEach(point -> point.setLocation(point.x + xShift, point.y));

        // add bottom floor
        IntStream.range(0, caveWidth).boxed().forEach(index -> wallPoints.add(new Point(index, caveHeight - 1)));

        Cave cave = new Cave(caveWidth, caveHeight, wallPoints);

        cave.print();
        System.out.println();

        int unitCount = 0;
        int x = pouringX;
        int y = 0;
        while (true) {

            if (cave.getTile(x, y + 1).isFree()) {
                y++;
            } else if (cave.getTile(x - 1, y + 1).isFree()) {
                x--;
                y++;
            } else if (cave.getTile(x + 1, y + 1).isFree()) {
                x++;
                y++;
            } else {
                cave.setTile(new Tile(Tile.Sprite.SAND), x, y);

                unitCount++;
                if (x == pouringX && y == 0) {
                    break;
                }
                x = pouringX;
                y = 0;
            }
        }
        cave.print();
        System.out.println("unitCount = " + unitCount);

    }

    private static Wall lineToWall(String value) {
        LinkedList<Point> pinPoints = new LinkedList<>(Arrays.stream(value.split(" -> ")).map(pinPoint -> {
            List<Integer> numbers = Arrays.stream(pinPoint.split(",")).map(Integer::parseInt).toList();
            return new Point(numbers.get(0), numbers.get(1));
        }).toList());
        return new Wall(pinPoints);
    }

    static Set<Point> getWallPoints(Point from, Point to) {
        int start;
        int end;
        Function<Integer, Point> toPoint;
        if (from.x < to.x) {
            start = from.x;
            end = to.x + 1;
            toPoint = item -> new Point(item, from.y);
        } else if (from.x > to.x) {
            start = to.x;
            end = from.x + 1;
            toPoint = item -> new Point(item, from.y);
        } else if (from.y < to.y) {
            start = from.y;
            end = to.y + 1;
            toPoint = item -> new Point(from.x, item);
        } else {
            start = to.y;
            end = from.y + 1;
            toPoint = item -> new Point(from.x, item);
        }
        return IntStream.range(start, end).boxed().map(toPoint).collect(
                Collectors.toSet());
    }

    record Wall(LinkedList<Point> pinPoints) {
    }

    static class Cave {

        private final Tile[][] tiles; // [y][x]

        public Cave(int width, int height, Set<Point> wallPoints) {
            tiles = new Tile[height][width];
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    int finalI = i;
                    int finalJ = j;
                    if (wallPoints.stream().anyMatch(item -> item.x == finalI && item.y == finalJ)) {
                        this.setTile(new Tile(Tile.Sprite.STONE), i, j);
                    } else {
                        this.setTile(new Tile(Tile.Sprite.AIR), i, j);
                    }
                }
            }
        }

        public void setTile(Tile tile, int x, int y) {
            tiles[y][x] = tile;
        }

        public Tile getTile(int x, int y) {
            return tiles[y][x];
        }

        public void print() {
            for (Tile[] tile : tiles) {
                for (Tile value : tile) {
                    System.out.print(value.toString());
                }
                System.out.println();
            }

        }
    }
}

class Tile {

    char sprite = '.';

    public Tile(Sprite sprite) {
        this.sprite = spriteToChar(sprite);
    }

    private static char spriteToChar(Sprite sprite) {
        return switch (sprite) {
            case AIR -> '.';
            case SAND -> '0';
            case STONE -> '#';
        };
    }

    public boolean isFree() {
        return sprite == '.';
    }

    @Override
    public String toString() {
        return String.valueOf(sprite);
    }

    enum Sprite {
        AIR, STONE, SAND
    }
}
