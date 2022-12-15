import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static java.lang.Math.abs;

public class Day9 {

    public static void main(String[] args) {
        FileReader<Move> fileReader = new FileReader<>("day9", Move::new);
        List<Move> moves = fileReader.readFile();
        Set<Point> tailVisited = new HashSet<>();
        Point tail = new Point(0, 0);
        Point head = new Point(tail);
        tailVisited.add(new Point(tail));

        moves.forEach(move -> {
            move(head, move);
            moveTo(tail, head, move.direction, point -> tailVisited.add(new Point(point)));
        });

        long tailVisitedCount = tailVisited.size();
        System.out.println("tailVisitedCount = " + tailVisitedCount);

        tailVisited.clear();
        List<Point> knots = IntStream.range(0, 10).boxed().map(item -> new Point(0, 0)).toList();
        Point header = knots.get(0);
        tailVisited.add(new Point(knots.get(8)));

        moves.forEach(move -> {
            move(header, move);
            // todo calc head position
            // move head by 1, adjust all other
            for (int i = 0; i < knots.size() - 2; i++) {
                Point current = knots.get(i);
                Point next = knots.get(i + 1);
                moveTo(next, current, move.direction, null);
            }
            Point preTail = knots.get(8);
            Point longTail = knots.get(9);
            while (detouched(preTail, longTail)) {
                // move tail
                switch (move.direction) {
                    case UP -> stepVertical(preTail.x, true, longTail);
                    case DOWN -> stepVertical(preTail.x, false, longTail);
                    case LEFT -> stepHorizontal(preTail.y, false, longTail);
                    case RIGHT -> stepHorizontal(preTail.y, true, longTail);
                }
                tailVisited.add(new Point(longTail));
            }

        });

        long longTailCount = tailVisited.size();
        System.out.println("long count = " + longTailCount);
    }

    static void moveTo(Point source, Point target, Direction direction, Consumer<Point> consumer) {
        while (detouched(source, target)) {
            // move tail
            switch (direction) {
                case UP -> stepVertical(target.x, true, source);
                case DOWN -> stepVertical(target.x, false, source);
                case LEFT -> stepHorizontal(target.y, false, source);
                case RIGHT -> stepHorizontal(target.y, true, source);
            }
            if (consumer != null) {
                consumer.accept(source);
            }
            //    System.out.println("tail moved to = " + tail);
        }
    }
    static void stepVertical(int xAdjustment, boolean up, Point point) {
        if (abs(xAdjustment - point.x) > 1) {
            throw new IllegalArgumentException("Should not adjust " + point + " to x=" + xAdjustment);
        }
        point.move(xAdjustment, point.y + (up ? 1 : -1));
    }

    static void stepHorizontal(int yAdjustment, boolean right, Point point) {
        if (abs(yAdjustment - point.y) > 1) {
            throw new IllegalArgumentException("Should not adjust " + point + " to y=" + yAdjustment);
        }
        point.move(point.x + (right ? 1 : -1), yAdjustment);
    }

    static void move(Point point, final Move move) {
        switch (move.direction) {
            case UP -> point.translate(0, move.size);
            case DOWN -> point.translate(0, -move.size);
            case LEFT -> point.translate(-move.size, 0);
            case RIGHT -> point.translate(move.size, 0);
        }
    }

    static boolean detouched(Point a, Point b) {
        return a.distance(b) > 1.5;
    }

    enum Direction {
        UP, DOWN, LEFT, RIGHT;

        public static Direction parse(String value) {
            return switch (value) {
                case "U" -> UP;
                case "D" -> DOWN;
                case "L" -> LEFT;
                case "R" -> RIGHT;
                default -> throw new IllegalArgumentException("Cannot create Direction from " + value);
            };
        }

    }

    static class Move {
        final Direction direction;
        final int size;

        Move(String value) {
            String[] split = value.split(" ");
            this.direction = Direction.parse(split[0]);
            this.size = Integer.parseInt(split[1]);
        }


    }


}
