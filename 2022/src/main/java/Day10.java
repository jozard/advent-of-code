import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

enum RegisterName {
    X
}

enum Type {
    ADD, NOOP
}

class Register {
    final RegisterName name;
    int value;

    public Register(RegisterName name, int value) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

public class Day10 {

    public static void main(String[] args) {
        FileReader<Command> fileReader = new FileReader<>("day10", Command::new);
        List<Command> commands = fileReader.readFile();

        Map<Integer, Integer> cyclesToStrength = new HashMap<>();
        IntStream.range(0, 6).boxed().map(item -> item * 40 + 20).forEach(item -> {
            cyclesToStrength.put(item, null);
        });
        AtomicInteger position = new AtomicInteger(0);
        final Register x = new Register(RegisterName.X, 1);
        final Sprite sprite = new Sprite(x);
        AtomicReference<String> line = new AtomicReference<>("");
        commands.forEach(command -> {
            for (int i = 1; i <= command.getCycles(); i++) {
                // -------- Draw phase ---------
                if (sprite.covers(position.get() % 40)) {
                    line.set(line.get().concat("#"));
                } else {
                    line.set(line.get().concat("."));
                }
                if ((position.get() + 1) % 40 == 0) {
                    System.out.println(line.getAndSet(""));
                }
                //
                position.incrementAndGet();

                // check if we have passed a marked cycle
                Optional<Integer> cyclePassed = cyclesToStrength.keySet().stream().sorted().filter(
                        item -> cyclesToStrength.get(item) == null).findFirst().filter(
                        item -> item < (position.get() + 1));

                // add current X value to the cycle strength found
                cyclePassed.ifPresent(key -> {
                    cyclesToStrength.put(key, x.value);
                });
            }

            RegisterName target = command.registerName;
            if (target != null && target.equals(RegisterName.X)) {
                if (command.type.equals(Type.ADD)) {
                    x.setValue(x.getValue() + command.value);
                }
            }

        });

        System.out.println("Strength = " + cyclesToStrength.entrySet().stream().map(
                entry -> entry.getKey() * entry.getValue()).reduce(Integer::sum).orElse(0));

    }

    record Sprite(Register x) {
        boolean covers(int position) {
            return x.value >= position - 1 && x.value <= position + 1;
        }
    }

    static class Command {
        final int value;
        final RegisterName registerName;

        final Type type;

        Command(String value) {
            String[] split = value.split(" ");
            boolean hasArguments = split.length > 1;
            this.value = hasArguments ? Integer.parseInt(split[1]) : 0;
            String command = split[0];
            String commandTarget = command.substring(command.length() - 1);
            boolean hasRegister = Arrays.stream(RegisterName.values()).map(RegisterName::name).anyMatch(
                    item -> item.equals(commandTarget.toUpperCase()));
            if (hasRegister) {
                registerName = RegisterName.valueOf(commandTarget.toUpperCase());
                command = command.substring(0, command.length() - 1);
            } else {
                registerName = null;
            }
            type = Type.valueOf(command.toUpperCase());
        }

        int getCycles() {
            return switch (type) {
                case ADD -> 2;
                case NOOP -> 1;
            };
        }
    }

}
