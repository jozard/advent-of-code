import java.text.MessageFormat;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Day15 {

//    static final long TARGET_LINE = 2000000;
    static final long TARGET_LINE = 26;

    public static void main(String[] args) {

        FileReader<Spot> fileReader = new FileReader<>("day15",
                (index, line) -> lineToSpot(line));
        List<Spot> spots = fileReader.readFile();
        System.out.println("Spots count: " + spots.size());
        Supplier<Stream<Long>> xCoordinateSupplier = () -> spots.stream().flatMap(
                spot -> Stream.of(spot.beacon, spot.sensor)).map(
                point -> point.x);
        long left = xCoordinateSupplier.get().min(Long::compare).orElseThrow();
        long right = xCoordinateSupplier.get().max(Long::compare).orElseThrow();
        System.out.println(MessageFormat.format("Cave dimensions: left={0} right={1}", left, right));

        Set<Location> targetLineLocations = LongStream.range(left, right + 1).boxed().map(
                index -> new Location(new Location(index, TARGET_LINE))).collect(Collectors.toSet());
        // replace empty locations with beacons and sensors
        spots.stream().flatMap(
                spot -> Stream.of(spot.beacon, spot.sensor)).filter(location -> location.y == TARGET_LINE).forEach(
                item -> {
                    targetLineLocations.remove(item);
                    targetLineLocations.add(item);
                }
        );

//        System.out.println(drawLine(targetLineLocations));

        spots.forEach((spot) -> {
            System.out.println(MessageFormat.format("Processing spot {0} - {1} ", spot.sensor, spot.beacon));
            Location a = spot.beacon;
            Location b = spot.sensor;
            long dX = a.x - b.x;
            long dY = a.y - b.y;
            double radius = Math.sqrt(dX*dX + dY*dY);

            double tY = TARGET_LINE;

            double targetX =  Math.sqrt(Math.abs(radius*radius - (b.y-tY)*(b.y-tY)));

            CircleWave wave = new CircleWave(spot.getSensor());
            while (!wave.step().contains(spot.beacon)) {
                System.out.print(
                        MessageFormat.format("\rWave y top = {0} bottom={1} target left = {2} right x = {3}",
                                wave.mostTop, wave.mostBottom, wave.targetLeft, wave.targetRight));

            }
            Optional<Long> leftWaveTargetLine = wave.getWave().stream().filter(point -> point.y == TARGET_LINE).map(
                    point -> point.x).min(Long::compare);
            if (leftWaveTargetLine.isPresent()) {
                long rightWaveTargetLine = wave.getWave().stream().filter(point -> point.y == TARGET_LINE).map(
                        point -> point.x).max(Long::compare).orElseThrow();
                LongStream.range(leftWaveTargetLine.get(), rightWaveTargetLine + 1).boxed().map(
                        index -> new Location(new Location(index, TARGET_LINE), Location.Type.COVERED)).forEach(
                        location -> {
                            if (targetLineLocations.stream().filter(
                                    targetLocation -> targetLocation.x == location.x).findFirst().orElseThrow().isEmpty()) {
                                // replace empty location with covered by this wave excluding beacons and sensors in targetLineLocations
                                targetLineLocations.remove(location);
                                targetLineLocations.add(location);
                            }
                        });
            }
//            System.out.println(drawLine(targetLineLocations));
        });

        System.out.println("Cannot contain beacon = " + targetLineLocations.stream().filter(
                location -> !(location.isEmpty() || location.isBeacon())).count());

    }

    private static Spot lineToSpot(String value) {
        Pattern pattern = Pattern.compile("(?:-?\\d+)+");
        Matcher matcher = pattern.matcher(value);
        List<Long> results = matcher.results().map(MatchResult::group).map(Long::parseLong).toList();
        if (results.size() != 4) {
            throw new IllegalStateException("Failed process line '" + value + "'");
        }
        Location sensor = new Location(results.get(0), results.get(1));
        Location beacon = new Location(results.get(2), results.get(3));
        return new Spot(sensor, beacon);
    }

    private static String drawLine(Set<Location> locations) {
        return locations.stream().sorted(Comparator.comparingLong(o -> o.x)).map(Location::getType).map(type ->
                type == Location.Type.BEACON ? "B" : (type == Location.Type.COVERED ? "#" : (type == Location.Type.SENSOR ? "S" : "."))
        ).collect(Collectors.joining());
    }


}

class CircleWave {
    final LinkedList<Location> overSignals;
    final LinkedList<Location> belowSignals;
    Location mostLeft;
    Location mostTop;
    Location mostBottom;
    Location targetLeft;
    Location mostRight;
    Location targetRight;

    public CircleWave(final Location waveStart) {
        this.overSignals = new LinkedList<>();
        overSignals.add(new Location(waveStart));
        mostTop = overSignals.get(0);
        this.belowSignals = new LinkedList<>();
        belowSignals.add(new Location(waveStart));
        mostBottom = belowSignals.get(0);
        this.mostLeft = new Location(waveStart);
        this.mostRight = new Location(waveStart);
    }

    Set<Location> step() {
        overSignals.add(new Location(mostLeft));
        overSignals.add(new Location(mostRight));
        belowSignals.add(new Location(mostRight));
        belowSignals.add(new Location(mostLeft));

        overSignals.parallelStream().forEach(point -> {
            point.move(point.x, point.y - 1);
            if (point.y == Day15.TARGET_LINE) {
                if (targetLeft == null || targetLeft.x > point.x) {
                    targetLeft = point;
                }
                if (targetRight == null || targetRight.x < point.x) {
                    targetRight = point;
                }
            }
        });
        belowSignals.parallelStream().forEach(point -> {
            point.move(point.x, point.y + 1);
            if (point.y == Day15.TARGET_LINE) {
                if (targetLeft == null || targetLeft.x > point.x) {
                    targetLeft = point;
                }
                if (targetRight == null || targetRight.x < point.x) {
                    targetRight = point;
                }
            }
        });

        mostRight.move(mostRight.x + 1, mostRight.y);
        mostLeft.move(mostLeft.x - 1, mostLeft.y);

        return getWave();
    }

    Set<Location> getWave() {
        Set<Location> wave = new HashSet<>();
        wave.addAll(new HashSet<>(overSignals));
        wave.addAll(new HashSet<>(belowSignals));
        wave.add(new Location(mostRight));
        wave.add(new Location(mostLeft));
        return wave;
    }
}

class Location {
    long x;
    long y;
    private Type type;

    public Location(Location p, Type type) {
        this(p.x, p.y);
        this.type = type;
    }

    public Location(Location p) {
        this(p, Type.EMPTY);
    }

    public Location(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public boolean isEmpty() {
        return type == Type.EMPTY;
    }

    public boolean isBeacon() {
        return type == Type.BEACON;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Location{" +
                "type=" + type +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    public void move(long x, long y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return x == location.x && y == location.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    enum Type {
        BEACON, SENSOR, COVERED, EMPTY
    }
}

class Spot {

    Location sensor;
    Location beacon;

    public Spot(Location sensor, Location beacon) {

        this.sensor = new Location(sensor, Location.Type.SENSOR);
        this.beacon = new Location(beacon, Location.Type.BEACON);
    }

    public Location getSensor() {
        return sensor;
    }

    public Location getBeacon() {
        return beacon;
    }
}
