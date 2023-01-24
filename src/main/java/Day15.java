import java.awt.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day15 {

    private static final int TARGET_LINE = 10;

    public static void main(String[] args) {

        FileReader<Spot> fileReader = new FileReader<>("day15",
                (index, line) -> lineToSpot(line));
        List<Spot> spots = fileReader.readFile();
        System.out.println("Spots count: " + spots.size());

        spots.forEach(spot -> {
            CircleWave wave = new CircleWave(spot.getSensor());
            while (!wave.step().contains(spot.beacon)) {

            }
            Set<Point> circle = wave.getWave();
        });

    }

    private static Spot lineToSpot(String value) {
        Pattern pattern = Pattern.compile("(?:-?\\d+)+");
        Matcher matcher = pattern.matcher(value);
        List<Integer> results = matcher.results().map(MatchResult::group).map(Integer::parseInt).toList();
        if (results.size() != 4) {
            throw new IllegalStateException("Failed process line '" + value + "'");
        }
        Point sensor = new Point(results.get(0), results.get(1));
        Point beacon = new Point(results.get(2), results.get(3));
        return new Spot(sensor, beacon);
    }


}

class CircleWave {
    final LinkedList<Point> overSignals;
    final LinkedList<Point> belowSignals;
    final Point mostLeft;
    final Point mostRight;

    public CircleWave(final Point waveStart) {
        this.overSignals = new LinkedList<>();
        overSignals.add(new Point(waveStart));
        this.belowSignals = new LinkedList<>();
        belowSignals.add(new Point(waveStart));
        this.mostLeft = new Point(waveStart);
        this.mostRight = new Point(waveStart);
    }

    Set<Point> step() {
        overSignals.forEach(point -> point.move(point.x, point.y - 1));
        belowSignals.forEach(point -> point.move(point.x, point.y + 1));
        overSignals.add(new Point(mostLeft.x, mostLeft.y - 1));
        overSignals.add(new Point(mostRight.x, mostRight.y - 1));
        belowSignals.add(new Point(mostLeft.x, mostLeft.y + 1));
        overSignals.add(new Point(mostRight.x, mostRight.y + 1));
        mostRight.move(mostRight.x + 1, mostRight.y);
        mostLeft.move(mostLeft.x - 1, mostLeft.y);
        return getWave();
    }

    Set<Point> getWave() {
        Set<Point> wave = new HashSet<>();
        wave.addAll(new HashSet<>(overSignals));
        wave.addAll(new HashSet<>(belowSignals));
        wave.add(new Point(mostRight));
        wave.add(new Point(mostLeft));
        return wave;
    }
}

class Spot {

    Point sensor;
    Point beacon;

    public Spot(Point sensor, Point beacon) {

        this.sensor = sensor;
        this.beacon = beacon;
    }

    public Point getSensor() {
        return sensor;
    }

    public Point getBeacon() {
        return beacon;
    }
}
