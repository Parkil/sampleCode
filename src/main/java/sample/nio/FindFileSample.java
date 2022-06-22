package sample.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FindFileSample {
    private static final Logger logger = LoggerFactory.getLogger(FindFileSample.class);

    public static void main(String[] args) throws Exception{
        Path basicPath = Path.of("src", "main", "java");
        List<Path> findList = findByFileName(basicPath, "Call.java");

        logger.info("findList : {}", findList);
    }

    public static List<Path> findByFileName(Path path, String fileName)
            throws IOException {

        List<Path> result;
        try (Stream<Path> pathStream = Files.find(path,
                Integer.MAX_VALUE,
                (p, basicFileAttributes) ->
                        p.getFileName().toString().equalsIgnoreCase(fileName))
        ) {
            result = pathStream.collect(Collectors.toList());
        }
        return result;

    }
}
