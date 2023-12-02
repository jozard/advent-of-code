import java.util.*;
import java.util.function.Function;

public class Day1 {
    public static void main(String[] args) {
        FileReader<String> fileReader = new FileReader<>("day1", Function.identity());
        System.out.println("Most calories = " + getMostCalories(fileReader.readFile()));
    }

    private static long getMostCalories(List<String> caloriesList) {

        Map<Long, List<Long>> caloriesMap = new TreeMap<>(Comparator.reverseOrder());
        List<Long> accumulator = new ArrayList<>();
        caloriesList.forEach(item -> {
            if (item.isBlank()) {
                caloriesMap.put(accumulator.stream().reduce(Long::sum).orElse(0L), accumulator);
                accumulator.clear();
            } else {
                accumulator.add(Long.parseLong(item));
            }
        });

        List<Long> top3Calories = caloriesMap.keySet().stream().limit(3).toList();
        System.out.println("top3Calories = " + top3Calories);
        return top3Calories.stream().reduce(Long::sum).orElse(0L);
    }

}
