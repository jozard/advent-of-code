import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;

public class Day1 {
    public static void main(String[] args) {
        String fileName = "day1";
        InputStream day1 = Day1.class.getClassLoader().getResourceAsStream(fileName);
        if (day1 == null) {
            throw new IllegalArgumentException(MessageFormat.format("File {0} not found", fileName));
        }
        System.out.println("Most calories = " + getMostCalories(readFile(day1)));
    }

    private static List<String> readFile(InputStream inputStream) {
        List<String> result = new ArrayList<>();
        try (InputStreamReader streamReader =
                     new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
        } catch (IOException e) {
            System.out.println(MessageFormat.format("Error reading file: {0}", e.getMessage()));
        }
        return result;
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
