import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Day8 {

    public static void main(String[] args) {
        FileReader<List<Tree>> fileReader = new FileReader<>("day8",
                line -> Arrays.stream(line.split("")).map(
                        value -> new Tree(Integer.parseInt(value))).toList());
        List<List<Tree>> forest = fileReader.readFile();

        int rowSize = forest.get(0).size();
        int columnSize = forest.size();

        for (int i = 0; i < rowSize; i++) { // row index
            Tree leftTallest = forest.get(i).get(0);
            leftTallest.setVisible();
            for (int j = 1; j < columnSize; j++) { // column index
                Tree current = forest.get(i).get(j);
                if (current.size > leftTallest.size) {
                    leftTallest = current;
                    leftTallest.setVisible();
                }
            }
            Tree rightTallest = forest.get(i).get(columnSize - 1); // column reverse index
            rightTallest.setVisible();
            for (int j = rowSize - 1; j >= 0; j--) {
                Tree current = forest.get(i).get(j);
                if (current.size > rightTallest.size) {
                    rightTallest = current;
                    rightTallest.setVisible();
                }
            }
        }

        for (int i = 0; i < forest.size(); i++) {  // column index
            Tree topTallest = forest.get(0).get(i);
            topTallest.setVisible();
            for (int j = 1; j < forest.size(); j++) { // row index
                Tree current = forest.get(j).get(i);
                if (current.size > topTallest.size) {
                    topTallest = current;
                    topTallest.setVisible();
                }
            }
            Tree bottomTallest = forest.get(forest.size() - 1).get(i);
            bottomTallest.setVisible();
            for (int j = forest.size() - 1; j >= 0; j--) {
                Tree current = forest.get(j).get(i);
                if (current.size > bottomTallest.size) {
                    bottomTallest = current;
                    bottomTallest.setVisible();
                }
            }
        }

        int visibleCount = forest.stream().flatMap(Collection::stream).filter(Tree::isVisible).toList().size();
        System.out.println("visibleCount = " + visibleCount);

    }

    static class Tree {
        final int size;
        boolean visible = false;


        Tree(int size) {
            this.size = size;
        }

        void setVisible() {
            this.visible = true;
        }

        public boolean isVisible() {
            return visible;
        }
    }


}
