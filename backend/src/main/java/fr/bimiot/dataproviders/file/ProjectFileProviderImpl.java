package fr.bimiot.dataproviders.file;

import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.providers.ProjectFileProvider;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Component
public class ProjectFileProviderImpl implements ProjectFileProvider {

    public final static Path PROJECTS_FOLDER = Paths.get("Projects");

    @Override
    public void create(Project project) throws DomainException, IOException {
        throwIfProjectAlreadyExist(project);
        createProjectFolder(project);
    }

    @Override
    public void delete(String projectName) throws DomainException {
        File file = checkExistence(projectName);
        deleteDirectory(file);
    }

    private void deleteDirectory(File file){
        if(file.isDirectory()){
            File[] subFiles = file.listFiles();
            if(subFiles != null){
                for (File subFile : subFiles){
                    deleteDirectory(subFile);
                }
            }
        }
        if(!file.delete()){
            throw new RuntimeException("Failed to delete" + file);
        }
    }

    private File checkExistence(String projectName) throws DomainException {
        Path projectPath = PROJECTS_FOLDER.resolve(projectName);
        if (Files.exists(projectPath)){
            return projectPath.toFile();
        }
        throw new DomainException("Le project " + projectName + " n'existe pas");
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
        if(project.getIfc() != null && project.getDataset() != null) {
            Files.copy(project.getIfc().getInputStream(), PROJECTS_FOLDER.resolve(project.getName()).resolve(Objects.requireNonNull(project.getIfc().getOriginalFilename())));
            Files.copy(project.getDataset().getInputStream(), PROJECTS_FOLDER.resolve(project.getName()).resolve(Objects.requireNonNull(project.getDataset().getOriginalFilename())));
        }
    }

    private void throwIfProjectDoesntExist(Project project) throws DomainException {
        if (!Files.isDirectory(PROJECTS_FOLDER.resolve(project.getName()))) {
            throw new DomainException("Le projet " + project.getName() + " n'existe pas !");
        }
    }
}
