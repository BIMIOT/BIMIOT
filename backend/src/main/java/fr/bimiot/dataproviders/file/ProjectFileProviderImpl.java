package fr.bimiot.dataproviders.file;

import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.providers.ProjectFileProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Component
public class ProjectFileProviderImpl implements ProjectFileProvider {

    private final static Path PROJECTS_FOLDER = Paths.get("Projects");

    @Override
    public void create(Project project) throws DomainException, IOException {
        throwIfProjectAlreadyExist(project);
        createProjectFolder(project);
    }

    private void throwIfProjectAlreadyExist(Project project) throws DomainException {
        if (Files.isDirectory(PROJECTS_FOLDER.resolve(project.getName()))) {
            throw new DomainException("Le projet '" + project.getName() + "' existe déjà !");
        }
    }

    private void createProjectFolder(Project project) throws IOException, DomainException {
        Files.createDirectory(PROJECTS_FOLDER.resolve(project.getName()));
        uploadProjectFile(project);
    }

    private void uploadProjectFile(Project project) throws DomainException, IOException {
        throwIfProjectDoesntExist(project);
        Files.copy(project.getIfc().getInputStream(), PROJECTS_FOLDER.resolve(project.getName()).resolve(Objects.requireNonNull(project.getIfc().getOriginalFilename())));
        Files.copy(project.getDataset().getInputStream(), PROJECTS_FOLDER.resolve(project.getName()).resolve(Objects.requireNonNull(project.getDataset().getOriginalFilename())));
    }

    private void throwIfProjectDoesntExist(Project project) throws DomainException {
        if (!Files.isDirectory(PROJECTS_FOLDER.resolve(project.getName()))) {
            throw new DomainException("Le projet " + project.getName() + " n'existe pas !");
        }
    }
}
