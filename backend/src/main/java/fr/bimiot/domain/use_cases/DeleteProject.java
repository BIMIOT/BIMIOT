package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.providers.ProjectDatabaseProvider;
import org.springframework.stereotype.Component;

@Component
public class DeleteProject {

    private final ProjectDatabaseProvider projectDatabaseProvider;

    public DeleteProject(ProjectDatabaseProvider projectDatabaseProvider) {
        this.projectDatabaseProvider = projectDatabaseProvider;
    }

    public void execute(String projectName) throws DomainException {
        projectDatabaseProvider.delete(projectName);
    }
}
