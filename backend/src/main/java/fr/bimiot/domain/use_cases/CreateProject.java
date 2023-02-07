package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.entities.ProjectDirectory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class CreateProject {
    public ProjectDirectory execute(ProjectDirectory projectDirectory){
        try{
            Files.createDirectory(Paths.get(projectDirectory.name()));
        }catch (Exception e){
            throw new AssertionError(e);
        }
        return projectDirectory;
    }
}
