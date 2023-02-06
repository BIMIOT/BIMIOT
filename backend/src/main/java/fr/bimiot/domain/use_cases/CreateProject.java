package fr.bimiot.domain.use_cases;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class CreateProject {
    public String execute(String directoryName){
        try{
            Files.createDirectory(Paths.get("backend/"+directoryName));
        }catch (Exception e){
            throw new AssertionError(e);
        }
        return directoryName;
    }
}
