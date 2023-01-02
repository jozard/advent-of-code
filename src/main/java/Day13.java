import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Day13 {

    public static void main(String[] args) {

        FileReader<Packet> fileReader = new FileReader<>("day13",
                (index, line) -> lineToPacket(line));
        List<List<Packet>> groups = fileReader.readFile("");

        List<Integer> rightOrderIndexes = new ArrayList<>();
        for (int i = 0; i < groups.size(); i++) {
            System.out.println("== Pair " + (i + 1) + " ==");
            Packet left = groups.get(i).get(0);
            Packet right = groups.get(i).get(1);

            Optional<Boolean> result = comparePackets(left, right);
            if (result.isPresent() && result.get()) {
                System.out.println(MessageFormat.format("Packets {0} and {1} are in right order", left, right));
                rightOrderIndexes.add(i + 1);
            }
        }
        System.out.println("rightOrderIndexes = " + rightOrderIndexes);
        System.out.println(
                "Right order indexes sum = " + rightOrderIndexes.stream().reduce(Integer::sum));
    }

    private static Optional<Boolean> comparePackets(Packet left, Packet right) {
        System.out.println(MessageFormat.format("Compare packets {0} and {1}", left, right));
        // both numbers
        if (left.getValue().isPresent() && right.getValue().isPresent()) {
            return compareNumbers(left.value, right.value);
        }
        // both lists
        if (left.getValue().isEmpty() && right.getValue().isEmpty()) {
            List<Packet> leftChildren = left.children;
            List<Packet> rightChildren = right.children;
            return compareLists(leftChildren, rightChildren);
        }
        // one is list
        if (left.getValue().isEmpty()) {
            System.out.println("Convert left to a right to a list");
            return compareLists(left.children, List.of(right));
        }
        System.out.println("Convert left to a right to a list");
        return compareLists(List.of(left), right.children);
    }

    private static Optional<Boolean> compareLists(List<Packet> leftChildren, List<Packet> rightChildren) {
        System.out.println(MessageFormat.format("Compare lists {0} and {1}", Arrays.toString(leftChildren.toArray()),
                Arrays.toString(rightChildren.toArray())));
        int compareLength = Math.min(leftChildren.size(), rightChildren.size());
        System.out.println("Compare " + compareLength + " first items");
        for (int i = 0; i < compareLength; i++) {
            Optional<Boolean> comparisonResult = comparePackets(leftChildren.get(i), rightChildren.get(i));
            if (comparisonResult.isPresent()) {
                return comparisonResult;
            }
        }
        if (leftChildren.size() < rightChildren.size()) {
            return Optional.of(true);
        }
        if (leftChildren.size() > rightChildren.size()) {
            return Optional.of(false);
        }
        return Optional.empty();
    }

    private static Optional<Boolean> compareNumbers(int left, int right) {
        return left == right ? Optional.empty() : Optional.of(left < right);
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

        public Packet(List<Packet> children) {
            this.children = children;
        }

        public Packet() {
            this.children = new ArrayList<>();
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