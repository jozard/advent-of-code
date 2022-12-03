import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FileReader<T> {

    private final Function<String, T> converter;
    private final String fileName;

    public FileReader(String fileName, Function<String, T> converter) {
        this.converter = converter;
        this.fileName = fileName;
    }

    public List<T> readFile() {
        InputStream fileStream = Day1.class.getClassLoader().getResourceAsStream(fileName);
        if (fileStream == null) {
            throw new IllegalArgumentException(MessageFormat.format("File {0} not found!", fileName));
        }
        return readFile(fileStream);
    }

    private List<T> readFile(InputStream inputStream) {
        List<T> result = new ArrayList<>();
        try (InputStreamReader streamReader =
                     new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(converter.apply(line));
            }
        } catch (IOException e) {
            System.out.println(MessageFormat.format("Error reading file: {0}", e.getMessage()));
        }
        return result;
    }
}
