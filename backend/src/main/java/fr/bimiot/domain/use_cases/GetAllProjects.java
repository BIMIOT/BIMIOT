package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.use_cases.providers.ProjectDatabaseProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetAllProjects {

    private final ProjectDatabaseProvider projectDatabaseProvider;

    public GetAllProjects(ProjectDatabaseProvider projectDatabaseProvider) {
        this.projectDatabaseProvider = projectDatabaseProvider;
    }

    public List<String> execute() {
        try {
            return Files.list(Paths.get("Projects"))
                    .filter(Files::isDirectory)
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("I/O error occurred");
        }
    }
}
