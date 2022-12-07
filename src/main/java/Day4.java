import java.util.List;

public class Day4 {
    public static void main(String[] args) {

        FileReader<Group> fileReader = new FileReader<>("day4", line ->
        {
            String[] sections = line.split(",");
            if (sections.length != 2) {
                throw new IllegalArgumentException("Could not convert to sections: " + line);
            }
            return new Group(toSection(sections[0]), toSection(sections[1]));
        });
        final List<Group> groups = fileReader.readFile();

        System.out.println("totalContaining = " + groups.stream().filter(
                group -> group.first.comprises(group.second) || group.second.comprises(group.first)).count());
        System.out.println("totalOverlapping = " + groups.stream().filter(
                group -> group.first.overlaps(group.second)).count());
    }

    private static Section toSection(String str) {
        String[] ids = str.split("-");
        if (ids.length != 2) {
            throw new IllegalArgumentException("Could not convert to section: " + str);
        }
        return new Section(Integer.parseInt(ids[0]), Integer.parseInt(ids[1]));
    }

    record Group(Section first, Section second) {
    }

    record Section(int start, int end) {
        boolean comprises(Section another) {
            return this.start <= another.start && this.end >= another.end;
        }

        boolean overlaps(Section another) {
            Section left;
            Section right;
            if (this.start <= another.start) {
                left = this;
                right = another;
            } else {
                left = another;
                right = this;
            }
            return left.comprises(right) || left.end >= right.start;

        }
    }
}
