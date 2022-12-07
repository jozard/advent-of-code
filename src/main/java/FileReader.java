import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class FileReader<T> {

    private final Function<String, T> lineConverter;
    private final String fileName;

    public FileReader(String fileName, Function<String, T> lineConverter) {
        this.lineConverter = lineConverter;
        this.fileName = fileName;
    }

    public List<T> readFile() {
        InputStream fileStream = getInputStream();
        List<T> result = new ArrayList<>();
        readStream(fileStream, line -> result.add(lineConverter.apply(line)));
        return result;
    }

    public List<List<T>> readFile(String groupSeparator) {
        InputStream fileStream = getInputStream();
        List<List<T>> result = new ArrayList<>();
        List<T> acc = new ArrayList<>();
        readStream(fileStream, line -> {
            if (groupSeparator.equals(line)) {
                result.add(acc);
                acc.clear();
            } else {
                acc.add(lineConverter.apply(line));
            }
        });
        return result;
    }

    public List<List<T>> readFile(int groupSize) {
        InputStream fileStream = getInputStream();
        List<List<T>> result = new ArrayList<>();
        List<T> acc = new ArrayList<>();
        readStream(fileStream, line -> {
            acc.add(lineConverter.apply(line));
            if (acc.size() == groupSize) {
                result.add(acc);
                acc.clear();
            }
        });
        return result;
    }

    private InputStream getInputStream() {
        InputStream fileStream = FileReader.class.getClassLoader().getResourceAsStream(fileName);
        if (fileStream == null) {
            throw new IllegalArgumentException(MessageFormat.format("File {0} not found!", fileName));
        }
        return fileStream;
    }

    private void readStream(InputStream stream, Consumer<String> consumer) {

        try (InputStreamReader streamReader = new InputStreamReader(stream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
                consumer.accept(line);
            }
        } catch (IOException e) {
            System.out.println(MessageFormat.format("Error reading file: {0}", e.getMessage()));
        }
    }
}
