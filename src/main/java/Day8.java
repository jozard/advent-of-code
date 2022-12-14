import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Day8 {

    public static void main(String[] args) {
        FileReader<List<Tree>> fileReader = new FileReader<>("day8",
                (index, line) -> {
                    String[] splitLine = line.split("");
                    List<Tree> result = new ArrayList<>(splitLine.length);
                    for (int i = 0; i < splitLine.length; i++) {
                        result.add(new Tree(Integer.parseInt(splitLine[i]), i, index));
                    }
                    return result;
                });
        List<List<Tree>> forest = fileReader.readFile();

        final int height = forest.size();
        final int width = forest.get(0).size();

        for (int i = 0; i < height; i++) { // row index
            parse(forest.get(i), true);
            parse(forest.get(i), false);
        }

        for (int i = 0; i < width; i++) {  // column index
            parse(getColumn(forest, i), true);
            parse(getColumn(forest, i), false);
        }

        int visibleCount = forest.stream().flatMap(Collection::stream).filter(Tree::isVisible).toList().size();
        System.out.println("visibleCount = " + visibleCount); // 1820

        int topScore = forest.stream().flatMap(Collection::stream).map(tree -> collectScore(tree, forest)).reduce(
                Integer::max).orElse(0);
        System.out.println("topScore = " + topScore); //385112

    }

    static List<Tree> getColumn(List<List<Tree>> forest, int index) {
        return forest.stream().flatMap(Collection::stream).filter(tree -> tree.x == index).toList();
    }

    static int collectScore(Tree target, List<List<Tree>> forest) {

        final long height = forest.size();
        final long width = forest.get(0).size();

        int rightScore = forest.get(target.y).stream().filter(
                tree -> tree.x > target.x && (tree.greaterOrEqual(target) || tree.x == width - 1)).map(
                tree -> tree.x - target.x).reduce(Integer::min).orElse(1);

        int leftScore = forest.get(target.y).stream().filter(
                tree -> tree.x < target.x && (tree.greaterOrEqual(target) || tree.x == 0)).map(
                tree -> target.x - tree.x).reduce(Integer::min).orElse(1);

        int upScore = getColumn(forest, target.x).stream().filter(
                tree -> tree.y < target.y && (tree.greaterOrEqual(target) || tree.y == 0)).map(
                tree -> target.y - tree.y).reduce(Integer::min).orElse(1);

        int downScore = getColumn(forest, target.x).stream().filter(
                tree -> tree.y > target.y && (tree.greaterOrEqual(target) || tree.y == height - 1)).map(
                tree -> tree.y - target.y).reduce(Integer::min).orElse(1);

        return rightScore * leftScore * upScore * downScore;
    }

    static void parse(List<Tree> row, boolean ascending) {
        Tree startTallest = ascending ? row.get(0) : row.get(row.size() - 1);
        startTallest.setVisible();
        if (ascending) {
            for (int j = 1; j < row.size(); j++) {
                startTallest = compare(startTallest, row.get(j));
            }
        } else {
            for (int j = row.size() - 1; j >= 0; j--) {
                startTallest = compare(startTallest, row.get(j));
            }
        }
    }

    static Tree compare(Tree current, Tree another) {
        if (another.size > current.size) {
            another.setVisible();
            return another;
        }
        return current;
    }

    static class Tree {
        final int size;
        final int x;
        final int y;
        boolean visible = false;


        Tree(int size, int x, int y) {
            this.size = size;
            this.x = x;
            this.y = y;
        }

        void setVisible() {
            this.visible = true;
        }

        public boolean isVisible() {
            return visible;
        }

        public boolean greaterOrEqual(Tree another) {
            return this.size >= another.size;
        }


    }


}
