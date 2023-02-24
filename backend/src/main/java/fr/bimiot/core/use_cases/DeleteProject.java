package fr.bimiot.core.use_cases;

import fr.bimiot.core.use_cases.providers.ProjectDatabaseProvider;
import fr.bimiot.dataproviders.database.ProjectDatabaseProviderImpl;
import org.springframework.stereotype.Component;

@Component
public class DeleteProject {

    private final ProjectDatabaseProviderImpl projectDatabaseProviderImpl;
    private final ProjectDatabaseProvider projectDatabaseProvider;

    public DeleteProject(ProjectDatabaseProviderImpl projectDatabaseProviderImpl, ProjectDatabaseProvider projectDatabaseProvider) {
        this.projectDatabaseProviderImpl = projectDatabaseProviderImpl;
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
