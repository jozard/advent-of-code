import java.text.MessageFormat;
import java.util.List;

public class Day2 {

    public static void main(String[] args) {
        // Day2 part 1
//        FileReader<Round> fileReader = new FileReader<>("day2", Round::decode);
        // Day2 part 2
        FileReader<Round> fileReader = new FileReader<>("day2", Round::elfDecode); // 12111
        System.out.println("Points = " + getWinningPoints(fileReader.readFile()));
    }

    private static long getWinningPoints(List<Round> rounds) {
        return rounds.stream().map(round -> {
            Result result = round.me.compare(round.opponent);
            return result.points + round.me.getPoints();
        }).reduce(Integer::sum).orElse(0);
    }

    /**
     * Returns a Hand object that gives the given {@code result} against the given {@code opponent}
     */
    static Hand getHandFor(Result result, Hand opponent) {
        return switch (result) {
            case LOSS -> opponent.winsTo();
            case DRAW -> opponent;
            case WIN -> opponent.loosesTo();
        };
    }

    enum Hand {
        ROCK, PAPER, SCISSORS;

        static Hand from(Character character) {
            return switch (character) {
                case 'A', 'X' -> ROCK;
                case 'B', 'Y' -> PAPER;
                case 'C', 'Z' -> SCISSORS;
                default -> throw new IllegalArgumentException(
                        MessageFormat.format("Cannot convert {0} to a Hand", character));
            };

        }

        int getPoints() {
            return this.ordinal() + 1;
        }

        Hand loosesTo() {
            return switch (this) {
                case ROCK -> PAPER;
                case PAPER -> SCISSORS;
                case SCISSORS -> ROCK;
            };
        }

        Hand winsTo() {
            return switch (this) {
                case ROCK -> SCISSORS;
                case PAPER -> ROCK;
                case SCISSORS -> PAPER;
            };
        }

        Result compare(Hand opponent) {
            if (this.equals(opponent)) {
                return Result.DRAW;
            }
            if (opponent.winsTo().equals(this)) {
                return Result.LOSS;
            }
            return Result.WIN;
        }
    }

    enum Result {
        WIN(6), DRAW(3), LOSS(0);
        private final int points;

        Result(int points) {
            this.points = points;
        }

        static Result decode(Character character) {
            return switch (character) {
                case 'X' -> LOSS;
                case 'Y' -> DRAW;
                case 'Z' -> WIN;
                default -> throw new IllegalArgumentException(
                        MessageFormat.format("Cannot convert {0} to a Result", character));
            };
        }
    }

    record Round(Hand opponent, Hand me) {
        static Round decode(String s) {
            String value = s.strip();
            if (value.length() != 3 || value.charAt(1) != ' ') {
                throw new IllegalArgumentException(MessageFormat.format("Wrong file format: ", value));
            }
            return new Round(Hand.from(value.charAt(0)), Hand.from(value.charAt(2)));
        }

        static Round elfDecode(String s) {
            String value = s.strip();
            if (value.length() != 3 || value.charAt(1) != ' ') {
                throw new IllegalArgumentException(MessageFormat.format("Wrong file format: ", value));
            }
            Hand opponent = Hand.from(value.charAt(0));
            Hand me = getHandFor(Result.decode(value.charAt(2)), opponent);
            return new Round(opponent, me);
        }
    }
}
