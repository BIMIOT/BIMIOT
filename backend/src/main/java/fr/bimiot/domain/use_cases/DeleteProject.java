package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.use_cases.providers.ProjectDatabaseProvider;
import org.springframework.stereotype.Component;

@Component
public class DeleteProject {

    private final ProjectDatabaseProvider projectDatabaseProvider;

    public DeleteProject(ProjectDatabaseProvider projectDatabaseProvider) {
        this.projectDatabaseProvider = projectDatabaseProvider;
    }

    /**
     * Delete project in database
     * @param projectName name of project
     */
    public void execute(String projectName) {
        projectDatabaseProvider.delete(projectName);
    }
}
