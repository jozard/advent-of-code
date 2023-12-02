import java.util.function.Function;

public class Day6 {
    
    public static void main(String[] args) {
        FileReader<String> fileReader = new FileReader<>("day6", Function.identity());
        String data = fileReader.readFile().get(0);
        int index = 14;
        while (index<data.length()) {
            String marker = data.substring(index-14, index);
            if (marker.chars().distinct().count() == 14) {
                break;
            } else {
                index++;
            }
        }
        System.out.println("Paket start: " + index);
    }


}
