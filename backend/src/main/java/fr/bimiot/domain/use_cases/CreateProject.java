package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.entities.ProjectDirectory;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class CreateProject {

    private final static String IFC_FILE = "ifcFile.ifc";

    public ProjectDirectory execute(ProjectDirectory projectDirectory){
        try{
            if(!Files.isDirectory(Paths.get("Projects"))){
                Files.createDirectory(Paths.get("Projects"));
            }
            Files.createDirectory(Paths.get("Projects/" + projectDirectory.name()));
            Files.createFile(Paths.get("Projects/" + projectDirectory.name() + "/" + IFC_FILE));
            Files.write(Paths.get("Projects/"+projectDirectory.name()+"/" + IFC_FILE), projectDirectory.ifcFile().getBytes());
        }catch (Exception e){
            throw new AssertionError(e);
        }
        return projectDirectory;
    }
}
