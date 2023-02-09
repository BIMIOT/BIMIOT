package fr.bimiot.domain.use_cases;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class GetFile {

    private final static Path PROJECTS_FOLDER = Paths.get("Projects");

    public byte[] execute(String projectName) {
        try {
            Path ifcFile = Files.find(PROJECTS_FOLDER.resolve(projectName), Integer.MAX_VALUE,
                    ((path, basicFileAttributes) -> path.toString().endsWith(".ifc"))).toList().get(0);
            return Files.readAllBytes(ifcFile);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
