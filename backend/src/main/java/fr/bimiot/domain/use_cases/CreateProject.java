package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.entities.ProjectDirectory;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class CreateProject {
    public ProjectDirectory execute(ProjectDirectory projectDirectory){
        try{
            if(!Files.isDirectory(Paths.get("Projects"))){
                Files.createDirectory(Paths.get("Projects"));
            }
            Files.createDirectory(Paths.get("Projects/" + projectDirectory.name()));
        }catch (Exception e){
            throw new AssertionError(e);
        }
        return projectDirectory;
    }
}
