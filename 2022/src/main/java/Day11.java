import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class Day11 {

    public static void main(String[] args) {
//        Monkey monkey0 = new Monkey(List.of(79, 98), level -> level * 19, 23);
//        Monkey monkey1 = new Monkey(List.of(54, 65, 75, 74), level -> level + 6, 19);
//        Monkey monkey2 = new Monkey(List.of(79, 60, 97), level -> level * level, 13);
//        Monkey monkey3 = new Monkey(List.of(74), level -> level + 3, 17);
        Monkey monkey0 = new Monkey(List.of(98, 89, 52), level -> level * 2, 5);
        Monkey monkey1 = new Monkey(List.of(57, 95, 80, 92, 57, 78), level -> level * 13, 2);
        Monkey monkey2 = new Monkey(List.of(82, 74, 97, 75, 51, 92, 83), level -> level + 5, 19);
        Monkey monkey3 = new Monkey(List.of(97, 88, 51, 68, 76), level -> level + 6, 7);
        Monkey monkey4 = new Monkey(List.of(63), level -> level + 1, 17);
        Monkey monkey5 = new Monkey(List.of(94, 91, 51, 63), level -> level + 4, 13);
        Monkey monkey6 = new Monkey(List.of(61, 54, 94, 71, 74, 68, 98, 83), level -> level + 2, 3);
        Monkey monkey7 = new Monkey(List.of(90, 56), level -> level * level, 11);

//        monkey0.setTrueMonkey(monkey2);
//        monkey0.setFalseMonkey(monkey3);
//        monkey1.setTrueMonkey(monkey2);
//        monkey1.setFalseMonkey(monkey0);
//
//        monkey2.setTrueMonkey(monkey1);
//        monkey2.setFalseMonkey(monkey3);
//        monkey3.setTrueMonkey(monkey0);
//        monkey3.setFalseMonkey(monkey1);
        monkey0.setTrueMonkey(monkey6);
        monkey0.setFalseMonkey(monkey1);
        monkey1.setTrueMonkey(monkey2);
        monkey1.setFalseMonkey(monkey6);
        monkey2.setTrueMonkey(monkey7);
        monkey2.setFalseMonkey(monkey5);
        monkey3.setTrueMonkey(monkey0);
        monkey3.setFalseMonkey(monkey4);
        monkey4.setTrueMonkey(monkey0);
        monkey4.setFalseMonkey(monkey1);
        monkey5.setTrueMonkey(monkey4);
        monkey5.setFalseMonkey(monkey3);
        monkey6.setTrueMonkey(monkey2);
        monkey6.setFalseMonkey(monkey7);
        monkey7.setTrueMonkey(monkey3);
        monkey7.setFalseMonkey(monkey5);

        List<Monkey> monkeys = List.of(monkey0, monkey1, monkey2, monkey3, monkey4, monkey5, monkey6, monkey7);
//        List<Monkey> monkeys = List.of(monkey0, monkey1, monkey2, monkey3);

        for (long round = 1; round <= 10_000; round++) {
            monkeys.forEach(monkey -> {
                List<Item> toRemove = new ArrayList<>();
                monkey.getItems().forEach(item -> {
//                    long newWorryLevel = (long) Math.floor(monkey.getOperationResult(item.worryLevel) / 3.0);
                    long newWorryLevel = monkey.getOperationResult(item.worryLevel);
                    Monkey targetMonkey = newWorryLevel % monkey.getTestParameter() == 0 ? monkey.getTrueMonkey() : monkey.getFalseMonkey();
//                    item.setWorryLevel(newWorryLevel);

//                    item.setWorryLevel(newWorryLevel % (23 * 19 * 13 * 17));
                    item.setWorryLevel(newWorryLevel % (5*2*19*7*17*13*3*11));

                    targetMonkey.addItem(item);
                    toRemove.add(item);
                });
                monkey.getItems().removeIf(toRemove::contains);
            });

//            if (round <= 20) {
//                monkeys.forEach(monkey -> System.out.println(
//                        MessageFormat.format("Monkey {0}: {1}", monkeys.indexOf(monkey),
//                                Arrays.toString(
//                                        monkey.getItems().stream().map(Item::getWorryLevel).toArray(Long[]::new)))));
//                System.out.println();
//            }

            if (round % 1000 == 0 || round == 20 || round == 1) {
                System.out.println("After round = " + round);
                monkeys.forEach(monkey -> System.out.println(
                        MessageFormat.format("Monkey {0} inspected items {1} times", monkeys.indexOf(monkey),
                                monkey.getInspectionCounter())));
                System.out.println();
                if (round == 20) {
                    Long monkeyBusiness = monkeys.stream().sorted(
                            Comparator.comparing(Monkey::getInspectionCounter)).skip(monkeys.size() - 2).map(
                            Monkey::getInspectionCounter).reduce((integer, integer2) -> integer * integer2).orElse(0L);
                    System.out.println("monkeyBusiness 20 = " + monkeyBusiness);
                }
            }
        }

        Long monkeyBusiness = monkeys.stream().sorted(Comparator.comparing(Monkey::getInspectionCounter)).skip(
                monkeys.size() - 2).map(Monkey::getInspectionCounter).reduce(
                (integer, integer2) -> integer * integer2).orElse(0L);
        System.out.println("monkeyBusiness all = " + monkeyBusiness);

    }

    static class Item {
        long worryLevel;

        public Item(long worryLevel) {
            this.worryLevel = worryLevel;
        }

        public long getWorryLevel() {
            return worryLevel;
        }

        public void setWorryLevel(long worryLevel) {
            this.worryLevel = worryLevel;
        }
    }

}

class Monkey {
    private final List<Day11.Item> items;
    private final Function<Long, Long> operation;
    private final Integer testParameter;
    private long inspectionCounter = 0;
    private Monkey trueMonkey;
    private Monkey falseMonkey;

    public Monkey(List<Integer> items, Function<Long, Long> operation, Integer testParameter) {
        this.items = new ArrayList<>(items.stream().map(Day11.Item::new).toList());
        this.operation = operation;
        this.testParameter = testParameter;
    }

    public List<Day11.Item> getItems() {
        return items;
    }

    void addItem(Day11.Item item) {
        this.items.add(item);
    }

    public Integer getTestParameter() {
        return testParameter;
    }

    public Monkey getTrueMonkey() {
        return trueMonkey;
    }

    public void setTrueMonkey(Monkey trueMonkey) {
        this.trueMonkey = trueMonkey;
    }

    public Monkey getFalseMonkey() {
        return falseMonkey;
    }

    public void setFalseMonkey(Monkey falseMonkey) {
        this.falseMonkey = falseMonkey;
    }

    public Long getOperationResult(Long input) {
        this.inspectionCounter++;
        return operation.apply(input);
    }

    public Long getInspectionCounter() {
        return inspectionCounter;
    }
}
