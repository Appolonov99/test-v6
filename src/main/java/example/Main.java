package example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        String filePath = "src/main/resources/test.txt";

        saveToFile(sortInNaturalOrder(readFile(filePath)), "result1.txt");
        saveToFile(sortByLength(readFile(filePath)), "result2.txt");
        if (Objects.nonNull(args) && args.length > 0 && Objects.nonNull(args[0])) {
            saveToFile(sortByWordNumber(readFile(filePath), Integer.parseInt(args[0])), "result3.txt");
        }
    }

    public static List<String> readFile(String filePath) {
        List<String> lines = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();

            while (line != null) {
                lines.add(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Возможно указан неверный путь до файла", e);
        }

        return lines;
    }

    public static List<String> sortInNaturalOrder(List<String> lines) {
        Collections.sort(lines, Comparator.naturalOrder());

        return countRepeatedStrings(lines);
    }

    public static List<String> sortByLength(List<String> lines) {
        lines.sort(Comparator.comparingInt(String::length));

        return countRepeatedStrings(lines);
    }

    public static List<String> sortByWordNumber(List<String> lines, int number) {
        try {
            Collections.sort(lines, (String lineA, String lineB) -> {
                String wordA2 = lineA.split(" ")[number - 1];
                String wordB2 = lineB.split(" ")[number - 1];
                return wordA2.compareTo(wordB2);
            });
        } catch (Exception e) {
            throw new RuntimeException("Введенный номер слова превышает количество слов в строке", e);
        }

        return countRepeatedStrings(lines);
    }

    public static List<String> countRepeatedStrings(List<String> lines) {
        Map<String, Integer> map = new HashMap<>();

        for(String word: lines) {
            if(!map.containsKey(word))
                map.put(word, 0);
            map.put(word, map.get(word) + 1);
        }

        return lines.stream().map(l -> l = l + " " + map.get(l)).collect(Collectors.toList());
    }

    public static void saveToFile(List<String> lines, String filename) {
        Path out = Paths.get(filename);
        try {
            Files.write(out, lines, Charset.defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException("Произошла ошибка при сохранении результата в файл", e);
        }
    }
}
