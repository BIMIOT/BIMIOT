package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.use_cases.providers.ProjectDatabaseProvider;
import org.springframework.stereotype.Component;

@Component
public class LoadIfcFile {
    private final ProjectDatabaseProvider projectDatabaseProvider;

    public LoadIfcFile(ProjectDatabaseProvider projectDatabaseProvider) {
        this.projectDatabaseProvider = projectDatabaseProvider;
    }

    public byte[] execute(String projectName){
        return projectDatabaseProvider.loadFile(projectName);
    }
}
