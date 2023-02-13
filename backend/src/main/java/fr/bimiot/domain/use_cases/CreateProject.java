package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.entities.ProjectDirectory;
import fr.bimiot.domain.exception.DomainException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Component
public class CreateProject {
    private final static Path PROJECTS_FOLDER = Paths.get("Projects");

    public void createFolder(ProjectDirectory projectDirectory) throws DomainException {
        if (Files.isDirectory(PROJECTS_FOLDER.resolve(projectDirectory.name()))) {
            throw new DomainException("Le projet '" + projectDirectory.name() + "' existe déjà !");
        }

        try {
            Files.createDirectory(PROJECTS_FOLDER.resolve(projectDirectory.name()));
        } catch (IOException e) {
            throw new RuntimeException("Le projet " + projectDirectory.name() + " n'a pas pu être créé !");
        }
    }

    public void uploadProjectFile(String projectName, MultipartFile file) throws DomainException {
        if (!Files.isDirectory(PROJECTS_FOLDER.resolve(projectName))) {
            throw new DomainException("Le projet " + projectName + " n'existe pas !");
        }
        try {
            Files.copy(file.getInputStream(), PROJECTS_FOLDER.resolve(projectName).resolve(Objects.requireNonNull(file.getOriginalFilename())));
        } catch (IOException e) {
            throw new RuntimeException("Le fichier " + file.getOriginalFilename() + " ne peut pas être stocké");
        }
    }
}
