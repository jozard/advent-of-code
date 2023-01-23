import java.awt.*;
import java.util.List;
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
