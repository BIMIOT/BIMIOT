package fr.bimiot.domain.use_cases;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetAllProjects {
    public List<String> execute() throws IOException {
        return Files.list(Paths.get("Projects"))
                .filter(Files::isDirectory)
                .map(path -> path.getFileName().toString())
                .collect(Collectors.toList());
    }
}
