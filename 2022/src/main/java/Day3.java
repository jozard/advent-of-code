import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day3 {
    public static void main(String[] args) {

        FileReader<Rucksack> fileReader = new FileReader<>("day3", Rucksack::new);
        final List<Rucksack> rucksacks = fileReader.readFile();

        AtomicLong totalPriority = new AtomicLong();
        rucksacks.forEach(rucksack -> totalPriority.addAndGet(rucksack.getFirst().stream().distinct()
                                                                      .filter(item -> rucksack.getSecond().stream()
                                                                                              .anyMatch(s -> s.equals(item)))
                                                                      .map(Rucksack.Item::getPriority).reduce(Integer::sum)
                                                                      .orElse(0))

        );

        System.out.println("totalPriority = " + totalPriority);

        int index = 0;
        List<List<Rucksack>> groupedRucksacks = new ArrayList<>();
        while (index < rucksacks.size()) {
            groupedRucksacks.add(rucksacks.subList(index, index += 3));
        }

        totalPriority.set(0);
        groupedRucksacks.forEach(group -> totalPriority.addAndGet(group.get(0).items.stream().distinct().filter(item ->
                                                                               group.get(1).items.contains(item) && group.get(2).items.contains(item)).map(Rucksack.Item::getPriority)
                                                                                    .reduce(Integer::sum).orElse(0)));
        System.out.println("totalPriorityGrouped = " + totalPriority);
    }

    static class Rucksack {

        private final List<Item> items;

        Rucksack(String content) {
            this.items = content.chars().mapToObj(ch -> new Rucksack.Item((char) ch)).toList();
        }

        List<Item> getFirst() {
            return items.subList(0, items.size() / 2);
        }

        List<Item> getSecond() {
            return items.subList(items.size() / 2, items.size());
        }

        static class Item {

            private final String priorities = Stream.concat(IntStream.rangeClosed('a', 'z').boxed(),
                                                            IntStream.rangeClosed('A', 'Z').boxed()).map(Character::toChars).map(String::valueOf)
                                                    .collect(Collectors.joining());

            private final char value;

            Item(char value) {
                this.value = value;
            }

            public char getValue() {
                return value;
            }

            public int getPriority() {
                // A-Z 65 - 90
                // a-z 97 - 122
                return priorities.indexOf(value) + 1;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) { return true; }
                if (o == null || getClass() != o.getClass()) { return false; }
                Item item = (Item) o;
                return value == item.value;
            }

            @Override
            public int hashCode() {
                return Objects.hash(value);
            }
        }
    }
}
