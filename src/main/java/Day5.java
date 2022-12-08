import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Day5 {

    // stacks[x][y] -> x - stack index, y - crate position bottom to top
    static List<List<String>> STACKS = List.of(
            List.of("S", "T", "H", "F", "W", "R"),
            List.of("S", "G", "D", "Q", "W"),
            List.of("B", "T", "W"),
            List.of("D", "R", "W", "T", "N", "Q", "Z", "J"),
            List.of("F", "B", "H", "G", "L", "V", "T", "Z"),
            List.of("L", "P", "T", "C", "V", "B", "S", "G"),
            List.of("Z", "B", "R", "T", "W", "G", "P"),
            List.of("N", "G", "M", "T", "C", "J", "R"),
            List.of("L", "G", "B", "W")
    );
//              [J] [Z] [G]
//              [Z] [T] [S] [P] [R]
//  [R]         [Q] [V] [B] [G] [J]
//  [W] [W]     [N] [L] [V] [W] [C]
//  [F] [Q]     [T] [G] [C] [T] [T] [W]
//  [H] [D] [W] [W] [H] [T] [R] [M] [B]
//  [T] [G] [T] [R] [B] [P] [B] [G] [G]
//  [S] [S] [B] [D] [F] [L] [Z] [N] [L]
//  1   2   3   4   5   6   7   8   9
    
    public static void main(String[] args) {
        FileReader<Instruction> fileReader = new FileReader<>("day5", line ->
        {
            String[] sections = line.split("move | from | to ");
            System.out.println(MessageFormat.format("Instruction {0} -> {1}", line, Arrays.stream(sections).toList()));
            if (sections.length != 4) {
                throw new IllegalArgumentException("Could not convert to sections: " + line);
            }
            return new Instruction(Integer.parseInt(sections[2]), Integer.parseInt(sections[1]), Integer.parseInt(sections[3]));
        });
        final List<Instruction> instructions = fileReader.readFile();
        List<String> stacks = move(STACKS, instructions, Day5::byOne);
        System.out.println("Top crates: " + stacks.stream().map(s -> s.substring(s.length() - 1)).collect(Collectors.joining()));
        stacks = move(STACKS, instructions, Day5::together);
        System.out.println("Top crates: " + stacks.stream().map(s -> s.substring(s.length() - 1)).collect(Collectors.joining()));
    }

    private static List<String> move(List<List<String>> stacks, List<Instruction> instructions,
                                     BiFunction<String, Integer, String> toMoveTransformer) {
        List<String> result = new ArrayList<>();
        stacks.forEach(stack -> result.add(String.join("", stack)));
        instructions.forEach(instruction -> {
            String sourceStack = result.get(instruction.sourceStack - 1);
            int splitIndex = sourceStack.length() - instruction.amount;
            final String bottom = sourceStack.substring(0, splitIndex);
            final String toMove = toMoveTransformer.apply(sourceStack, splitIndex);
            result.set(instruction.sourceStack - 1, bottom);
            result.set(instruction.targetStack - 1, result.get(instruction.targetStack - 1) + toMove);
        });
        return result;
    }

    private static String byOne(String sourceStack, int topItemsAmount) {
        return new StringBuilder(sourceStack.substring(topItemsAmount)).reverse().toString();
    }

    private static String together(String sourceStack, int topItemsAmount) {
        return sourceStack.substring(topItemsAmount);
    }

    record Instruction(int sourceStack, int amount, int targetStack) {
    }
}
