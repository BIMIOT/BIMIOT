package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.use_cases.providers.ProjectDatabaseProvider;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class GetAllProjects {
    private final ProjectDatabaseProvider projectDatabaseProvider;

    public GetAllProjects(ProjectDatabaseProvider projectDatabaseProvider) {
        this.projectDatabaseProvider = projectDatabaseProvider;
    }

    public List<String> execute() {
        return projectDatabaseProvider.getAllProjects();
    }
}
