import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day13 {

    public static void main(String[] args) {

        FileReader<Packet> fileReader = new FileReader<>("day13",
                (index, line) -> lineToPacket(line));
        List<List<Packet>> groups = fileReader.readFile("");

        List<Integer> rightOrderIndexes = new ArrayList<>();
        for (int i = 0; i < groups.size(); i++) {
            Packet left = groups.get(i).get(0);
            Packet right = groups.get(i).get(1);

            int result = comparePackets(left, right);
            if (result < 0) {
                rightOrderIndexes.add(i + 1);
            }
        }

        // -- Part 2
        List<Packet> dividers = List.of(
                new Packet(List.of(new Packet(List.of(new Packet(2))))),
                new Packet(List.of(new Packet(List.of(new Packet(6))))
                ));
        dividers.forEach(Packet::setDivider);


        List<Packet> sourcePackets = groups.stream().flatMap(Collection::stream).toList();
        List<Packet> sortedPackets = Stream.concat(dividers.stream(), sourcePackets.stream()).sorted(
                Day13::comparePackets).toList();
        System.out.println("Source packets: ");
        sourcePackets.forEach(System.out::println);
        System.out.println();

        System.out.println("sortedPackets: ");
        sortedPackets.forEach(System.out::println);
        System.out.println();
        int[] dividerIndexes = IntStream.range(0, sortedPackets.size()).filter(
                i -> sortedPackets.get(i).divider).map(item -> item + 1).toArray();
        System.out.println("dividerIndexes = " + Arrays.toString(dividerIndexes));
        int decoderKey = Arrays.stream(dividerIndexes).reduce((left, right) -> left * right).orElseThrow();

        System.out.println("rightOrderIndexes = " + rightOrderIndexes);
        System.out.println(
                "Right order indexes sum = " + rightOrderIndexes.stream().reduce(Integer::sum));
        System.out.println("decoderKey = " + decoderKey);

    }

    private static int comparePackets(Packet left, Packet right) {
        // both numbers
        if (left.getValue().isPresent() && right.getValue().isPresent()) {
            return Integer.compare(left.value, right.value);
        }
        // both lists
        if (left.getValue().isEmpty() && right.getValue().isEmpty()) {
            List<Packet> leftChildren = left.children;
            List<Packet> rightChildren = right.children;
            return compareLists(leftChildren, rightChildren);
        }
        // one is list
        if (left.getValue().isEmpty()) {
            return compareLists(left.children, List.of(right));
        }
        return compareLists(List.of(left), right.children);
    }

    private static int compareLists(List<Packet> leftChildren, List<Packet> rightChildren) {
        int compareLength = Math.min(leftChildren.size(), rightChildren.size());
        for (int i = 0; i < compareLength; i++) {
            int comparisonResult = comparePackets(leftChildren.get(i), rightChildren.get(i));
            if (comparisonResult != 0) {
                return comparisonResult;
            }
        }
        return Integer.compare(leftChildren.size(), rightChildren.size());
    }

    private static List<Packet> parseListPacket(String value) {
        int check = findClosingArrayIndex(value, 0);
        if (check != value.length() - 1) {
            throw new IllegalArgumentException("parseListPacket failed for '" + value + "'");
        }
        String rawValue = value.substring(1, value.length() - 1);
        if (rawValue.equals("")) {
            return List.of(new Packet());
        }

        List<Packet> result = new ArrayList<>();
        Optional<String> acc = Optional.empty();
        for (int i = 0; i < rawValue.length(); ) {
            char current = rawValue.charAt(i);
            if (current == '[') { // item is an array
                int itemEnd = findClosingArrayIndex(rawValue, i);
                List<Packet> itemChildren = parseListPacket(rawValue.substring(i, itemEnd + 1));
                result.add(new Packet(itemChildren));
                i = itemEnd + 1;
            } else if (current == ',') {
                if (acc.isPresent()) {
                    Packet item = new Packet();
                    item.setValue(Integer.parseInt(acc.get()));
                    result.add(item);
                    acc = Optional.empty();
                }
                i++;
            } else {
                acc = acc.map(s -> s + current).or(() -> Optional.of(String.valueOf(current)));
                if (i == rawValue.length() - 1) {
                    Packet item = new Packet();
                    item.setValue(Integer.parseInt(acc.get()));
                    result.add(item);
                }
                i++;
            }
        }
        return result;
    }

    private static Packet lineToPacket(String value) {
        return new Packet(parseListPacket(value));
    }

    private static int findClosingArrayIndex(String value, int openingIndex) {
        if (value.charAt(openingIndex) != '[') {
            throw new IllegalArgumentException("Find closing array index failed for '" + value + "'");
        }
        int bracketCount = 1;
        for (int i = openingIndex + 1; i < value.length(); i++) {
            char current = value.charAt(i);
            if (current == '[') {
                bracketCount++;
            } else if (current == ']') {
                bracketCount--;
                if (bracketCount == 0) {
                    return i;
                }
            }
        }
        if (value.charAt(value.length() - 1) == ']') {
            return value.length() - 1;
        }
        throw new IllegalArgumentException("Find closing array index failed for '" + value + "'");
    }

    static class Packet {
        private Integer value = null;
        private List<Packet> children;
        private boolean divider = false;

        public Packet(List<Packet> children) {
            this.children = children;
        }

        public Packet(Integer value) {
            this.value = value;
        }

        public Packet() {
            this.children = new ArrayList<>();
        }

        public void setDivider() {
            this.divider = true;
        }

        public Optional<Integer> getValue() {
            return Optional.ofNullable(value);
        }

        public void setValue(Integer value) {
            this.value = value;
            this.children = List.of();
        }

        public List<Packet> getChildren() {
            return children;
        }

        public void setChildren(List<Packet> children) {
            this.children = children;
            value = null;
        }

        @Override
        public String toString() {
            return getValue().isPresent() ? String.valueOf(value) : Arrays.toString(children.toArray());
        }
    }
}