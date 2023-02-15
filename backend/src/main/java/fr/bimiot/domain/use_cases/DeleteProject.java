package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.exception.DomainException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class DeleteProject {

    private final static Path PROJECTS_FOLDER = Paths.get("Projects");


    public void execute(String projectName) throws DomainException {
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

}
