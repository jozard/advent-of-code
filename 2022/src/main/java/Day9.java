import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

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

public class Day9 {

    public static void main(String[] args) {
        FileReader<Move> fileReader = new FileReader<>("day9", Move::new);
        List<Move> moves = fileReader.readFile();
        Set<Point> tailVisited = new HashSet<>();
        Point tail = new Point(0, 0);
        Point head = new Point(tail);
        tailVisited.add(new Point(tail));

        moves.forEach(move -> {
            Point headPosition = getPositionAfterFullMove(head, move);
            while (!head.equals(headPosition)) {
                move(head, getSingleStepDelta(move.direction));
                Optional<Delta> deltaToTouch = getSingleStepDelta(tail, head);
                if (deltaToTouch.isPresent()) {
                    move(tail, deltaToTouch.get());
                    tailVisited.add(new Point(tail));
                }
            }
        });

        long tailVisitedCount = tailVisited.size();
        System.out.println("tailVisitedCount = " + tailVisitedCount);

        tailVisited.clear();
        List<Point> knots =
                IntStream.range(0, 10).boxed().map(item -> new Point(0, 0)).toList();
        Point first = knots.get(0);
        Point last = new Point(knots.get(9));
        tailVisited.add(last);

        moves.forEach(move -> {
            Point headPosition = getPositionAfterFullMove(first, move);
            while (!first.equals(headPosition)) {
                move(first, getSingleStepDelta(move.direction));
                for (int i = 1; i < knots.size(); i++) {
                    Point prev = knots.get(i - 1);
                    Point curr = knots.get(i);
                    Optional<Delta> deltaToTouch = getSingleStepDelta(curr, prev);
                    if (deltaToTouch.isPresent()) {
                        move(curr, deltaToTouch.get());
                        if (i == knots.size() - 1) {
                            tailVisited.add(new Point(curr));
                        }
                    }

                }
            }
        });

        long longTailCount = tailVisited.size();
        System.out.println("long count = " + longTailCount);
    }

    private static void printKnots(List<Point> knots, int size) {
        System.out.println();
        for (int y = size; y >= -(size - 1); y--) {
            for (int x = -(size - 1); x < size; x++) {
                int finalX = x;
                int finalY = y;
                Optional<Point> found = knots.stream().filter(
                        knot -> knot.equals(new Point(finalX, finalY))).findFirst();
                if (found.isPresent()) {
                    if (knots.indexOf(found.get()) == 0) {
                        System.out.print("H");
                    } else {
                        System.out.print(knots.indexOf(found.get()));
                    }
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    static Delta getSingleStepDelta(Direction direction) {
        return switch (direction) {
            case UP -> new Delta(0, 1);
            case DOWN -> new Delta(0, -1);
            case LEFT -> new Delta(-1, 0);
            case RIGHT -> new Delta(1, 0);
        };
    }

    private static void move(Point point, Delta delta) {
        point.translate(delta.x(), delta.y());
    }

    static Point getPositionAfterFullMove(final Point point, final Move move) {
        Point result = new Point(point);
        switch (move.direction) {
            case UP -> result.translate(0, move.size);
            case DOWN -> result.translate(0, -move.size);
            case LEFT -> result.translate(-move.size, 0);
            case RIGHT -> result.translate(move.size, 0);
        }
        return result;
    }

    static boolean detouched(Point a, Point b) {
        return a.distance(b) > 1.5;
    }

    static Optional<Delta> getSingleStepDelta(Point source, Point target) {
        if (!detouched(source, target)) {
            return Optional.empty();
        }
        double theta = Math.atan2(target.y - source.y, target.x - source.x);
        boolean positive = theta > 0;
        int x = 0;
        int y = 0;
        if (Math.abs(theta) < 0.4) {
            x = 1;
        } else if (Math.abs(theta) > 0.4 && Math.abs(theta) < 1.5) {
            x = 1;
            y = positive ? 1 : -1;
        } else if (Math.abs(theta) > 1.5 && Math.abs(theta) < 1.6) {
            y = positive ? 1 : -1;
        } else if (Math.abs(theta) > 1.6 && Math.abs(theta) < 3) {
            x = -1;
            y = positive ? 1 : -1;
        } else if (Math.abs(theta) > 3) {
            x = -1;
        }
        return Optional.of(new Delta(x, y));
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

    record Delta(int x, int y) {
    }
}
