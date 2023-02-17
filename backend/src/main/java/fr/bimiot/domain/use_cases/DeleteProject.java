package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.providers.ProjectDatabaseProvider;
import fr.bimiot.domain.use_cases.providers.ProjectFileProvider;
import org.springframework.stereotype.Component;

@Component
public class DeleteProject {

    private final ProjectFileProvider projectFileProvider;

    private final ProjectDatabaseProvider projectDatabaseProvider;

    public DeleteProject(ProjectFileProvider projectFileProvider, ProjectDatabaseProvider projectDatabaseProvider) {
        this.projectFileProvider = projectFileProvider;
        this.projectDatabaseProvider = projectDatabaseProvider;
    }

    public void execute(String projectName) throws DomainException {
        projectFileProvider.delete(projectName);
        projectDatabaseProvider.delete(projectName);
    }
}
