import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class FileReader<T> {

    private final String fileName;
    private Function<String, T> lineConverter;
    private BiFunction<Integer, String, T> countingLineConverter;

    public FileReader(String fileName, Function<String, T> lineConverter) {
        this.lineConverter = lineConverter;
        this.fileName = fileName;
    }

    public FileReader(String fileName, BiFunction<Integer, String, T> lineConverter) {
        this.countingLineConverter = lineConverter;
        this.fileName = fileName;
    }

    public List<T> readFile() {
        InputStream fileStream = getInputStream();
        List<T> result = new ArrayList<>();

        readStream(fileStream, (index, line) -> {
            if (countingLineConverter != null) {
                result.add(countingLineConverter.apply(index, line));
            } else {
                result.add(lineConverter.apply(line));
            }
        });
        return result;
    }

    public List<List<T>> readFile(String groupSeparator) {
        InputStream fileStream = getInputStream();
        List<List<T>> result = new ArrayList<>();
        List<T> acc = new ArrayList<>();
        readStream(fileStream, (index, line) -> {
            if (groupSeparator.equals(line)) {
                result.add(acc);
                acc.clear();
            } else {
                if (countingLineConverter != null) {
                    acc.add(countingLineConverter.apply(index, line));
                } else {
                    acc.add(lineConverter.apply(line));
                }

            }
        });
        return result;
    }

    public List<List<T>> readFile(int groupSize) {
        InputStream fileStream = getInputStream();
        List<List<T>> result = new ArrayList<>();
        List<T> acc = new ArrayList<>();
        readStream(fileStream, (index, line) -> {
            if (countingLineConverter != null) {
                acc.add(countingLineConverter.apply(index, line));
            } else {
                acc.add(lineConverter.apply(line));
            }
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

    private void readStream(InputStream stream, BiConsumer<Integer, String> consumer) {

        try (InputStreamReader streamReader = new InputStreamReader(stream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {
            String line;
            int index = 0;
            while ((line = reader.readLine()) != null) {
                consumer.accept(index++, line);
            }
        } catch (IOException e) {
            System.out.println(MessageFormat.format("Error reading file: {0}", e.getMessage()));
        }
    }
}
