import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Day12 {

    public static void main(String[] args) {
        List<Square> startSquares = new ArrayList<>();
        AtomicReference<Square> endSquareRef = new AtomicReference<>();
        FileReader<Square[]> fileReader = new FileReader<>("day12",
                (index, line) -> {
                    Square[] result = new Square[line.length()];
                    for (int i = 0; i < line.length(); i++) {
                        char elevation = line.charAt(i);
                        result[i] = new Square(i, index,
                                elevation == 'S' ? 'a' : (elevation == 'E' ? 'z' : elevation));
                        if (elevation == 'S' || elevation == 'a') {
                            startSquares.add(result[i]);
                        } else if (elevation == 'E') {
                            endSquareRef.set(result[i]);
                        }
                    }
                    return result;
                });
        List<Square[]> squaresList = fileReader.readFile();
        // [row][column]
        Square[][] squares = new Square[squaresList.size()][];
        for (int i = 0; i < squaresList.size(); i++) {
            squares[i] = squaresList.get(i);
        }

        AtomicInteger lowestDistance = new AtomicInteger(Integer.MAX_VALUE);

        startSquares.forEach(startSquare -> {
            startSquare.setLowestDistance(0);
            Square evaluationNode = startSquare;
            dijkstra(squares, evaluationNode);

            System.out.println(MessageFormat.format("Distance from {0} to end = {1}", startSquare,
                    endSquareRef.get().getLowestDistance()));
            if (lowestDistance.get() > endSquareRef.get().getLowestDistance()) {
                lowestDistance.set(endSquareRef.get().getLowestDistance());
            }
            resetDistances(squares);
        });
        System.out.println("lowestDistance = " + lowestDistance.get());
    }

    private static void dijkstra(Square[][] squares, Square startNode) {
        HashSet<Square> unsettled = new HashSet<>();
        HashSet<Square> settled = new HashSet<>();
        settled.add(startNode);
        Square evaluationNode = startNode;
        do {
            // get neighbours of evaluated node avoiding settled
            Square finalEvaluationNode = evaluationNode;
            List<Square> filteredNeighbours = getNeighbours(finalEvaluationNode, squares).stream().filter(
                            item -> item.reachableFrom(finalEvaluationNode))
                    .filter(item -> !settled.contains(item))
                    .toList();
            // update or set distance for them
            int newDistance = finalEvaluationNode.getLowestDistance() + 1;
            filteredNeighbours.forEach(item -> {
                if (item.getLowestDistance() > newDistance) {
                    item.setLowestDistance(newDistance);
                }
            });
            // add them to unsettled
            unsettled.addAll(filteredNeighbours.stream().filter(item -> !settled.contains(item)).toList());
            unsettled.remove(finalEvaluationNode);
            settled.add(finalEvaluationNode);
            // assign next evaluation node
            evaluationNode = unsettled.stream().min(Comparator.comparing(Square::getLowestDistance)).orElse(null);
        } while (evaluationNode != null);
    }

    static List<Square> getNeighbours(Square target, Square[][] squares) {
        int maxRow = squares.length - 1;
        int maxColumn = squares[0].length - 1;
        List<Square> result = new ArrayList<>();
        int row = target.row;
        int column = target.column;
        if (row > 0) {
            result.add(squares[row - 1][column]);
        }
        if (row < maxRow) {
            result.add(squares[row + 1][column]);
        }
        if (column > 0) {
            result.add(squares[row][column - 1]);
        }
        if (column < maxColumn) {
            result.add(squares[row][column + 1]);
        }
        return result;
    }

    private static void resetDistances(Square[][] squares) {
        Arrays.stream(squares).flatMap(Arrays::stream).forEach(square -> square.setLowestDistance(Integer.MAX_VALUE));
    }

}

class Square {
    final int column;
    final int row;
    private final char elevation;

    private int lowestDistance = Integer.MAX_VALUE;

    Square(int column, int row, char elevation) {
        this.column = column;
        this.row = row;
        this.elevation = elevation;
    }

    public int getLowestDistance() {
        return lowestDistance;
    }

    public void setLowestDistance(int lowestDistance) {
        this.lowestDistance = lowestDistance;
    }

    @Override
    public String toString() {
        return "Square{" +
                "column=" + column +
                ", row=" + row +
                ", elevation=" + elevation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Square square = (Square) o;
        return column == square.column && row == square.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(column, row);
    }

    boolean reachableFrom(Square source) {
        return source.elevation + 1 >= elevation;
    }
}
