import java.text.MessageFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day7 {

    public static void main(String[] args) {
        FileReader<String> fileReader = new FileReader<>("day7", Function.identity());
        List<String> lines = fileReader.readFile();
        var ref = new Object() {
            Node root = null;
            Node currentNode = null;
        };
        lines.forEach(line -> {
            if (isCommand(line)) {
                Command command = Command.from(line);
                if (command.isNavigation()) {
                    if (ref.currentNode == null) {
                        ref.root = new Node(command.target, 0, null);
                        ref.currentNode = ref.root;
                    } else if (Objects.equals(command.target, "..")) {
                        ref.currentNode = ref.currentNode.parent;
                    } else {
                        String currentNodeName = ref.currentNode.name;
                        ref.currentNode = ref.currentNode.children.stream().filter(
                                        child -> Objects.equals(child.name, command.target))
                                .findFirst().orElseThrow(() -> new IllegalStateException(
                                        MessageFormat.format("Cannot navigate to {0} in {1}", command.target,
                                                currentNodeName)));
                    }
                }
            } else {
                Node.initNode(line, ref.currentNode);
            }
        });
        Map<Node, Long> nodeSizes = new HashMap<>();
        collectNodeSizes(ref.root, nodeSizes);
        nodeSizes.forEach((node, aLong) -> {
            System.out.println(MessageFormat.format("{0} size = {1}", node.getPath(), aLong));
        });
        Long result = nodeSizes.entrySet().stream().filter(
                entry -> entry.getKey().isFolder() && entry.getValue() <= 100000).map(
                Map.Entry::getValue).reduce(Long::sum).orElse(
                0L);
        System.out.println("result = " + result);
        long emptySpace = 70000000 - nodeSizes.get(ref.root);
        long requiredEmptySpace = 30000000;
        Long smallestToRemove = nodeSizes.entrySet().stream().filter(
                entry -> entry.getKey().isFolder() && (emptySpace + entry.getValue() >= requiredEmptySpace)).map(
                Map.Entry::getValue).sorted().findFirst().orElse(0L);
        System.out.println("smallestToRemove = " + smallestToRemove);
    }

    static long collectNodeSizes(Node source, Map<Node, Long> sizes) {
        long size = source.children.stream().mapToLong(node -> collectNodeSizes(node, sizes)).reduce(Long::sum).orElse(
                0) + source.size;
        sizes.put(source, size);
        return size;
    }

    static boolean isCommand(String line) {
        return line.strip().startsWith("$");
    }

    record Command(String target, String name) {

        Command(String target, String name) {
            this.target = target;
            this.name = name.strip();
        }

        public static Command from(String line) {
            String[] command = line.substring(2).split(" ");
            return command.length == 1 ? new Command(null, command[0]) : new Command(command[1], command[0]);
        }

        boolean isNavigation() {
            return Objects.equals(name, "cd");
        }

        boolean isListing() {
            return Objects.equals(name, "ls");
        }
    }


    static class Node {
        private final String name;
        private final Node parent;
        private final LinkedList<Node> children = new LinkedList<>();
        private long size = 0;

        Node(String name, long size, Node parent) {
            this.name = name;
            this.size = size;
            this.parent = parent;
            if (parent != null) {
                parent.addChild(this);
            }
        }

        public static void initNode(String line, Node parent) {
            String[] file = line.split(" ");
            long size = Objects.equals(file[0], "dir") ? 0 : Long.parseLong(file[0]);
            new Node(file[1], size, parent);
        }

        boolean isFolder() {
            return size == 0;
        }

        String getPath() {
            if (parent == null) {
                return name;
            }
            return parent.getPath() + ":" + name;
        }

        void addChild(Node child) {
            this.children.add(child);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "name='" + name + '\'' +
                    ", parent=" + (parent == null ? null : parent.name) +
                    ", size=" + size +
                    ", children=[" + children.stream().map(Node::toString).collect(
                    Collectors.joining(", ")) +
                    "]}";
        }
    }


}
