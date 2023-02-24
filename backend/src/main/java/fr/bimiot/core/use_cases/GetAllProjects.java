package fr.bimiot.core.use_cases;

import fr.bimiot.core.use_cases.providers.ProjectDatabaseProvider;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class GetAllProjects {
    private final ProjectDatabaseProvider projectDatabaseProvider;

    public GetAllProjects(ProjectDatabaseProvider projectDatabaseProvider) {
        this.projectDatabaseProvider = projectDatabaseProvider;
    }

    /**
     * Get all project's name
     * @return list of all project's names
     */
    public List<String> execute() {
        return projectDatabaseProvider.getAllProjects();
    }
}
