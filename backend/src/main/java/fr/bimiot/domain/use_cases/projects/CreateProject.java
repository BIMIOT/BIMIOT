package fr.bimiot.domain.use_cases.projects;

import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.providers.ProjectDatabaseProvider;
import fr.bimiot.domain.use_cases.providers.ProjectFileProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CreateProject {

    private final ProjectDatabaseProvider projectDatabaseProvider;
    private final ProjectFileProvider projectFileProvider;

    public CreateProject(ProjectDatabaseProvider projectDatabaseProvider, ProjectFileProvider projectFileProvider) {
        this.projectDatabaseProvider = projectDatabaseProvider;
        this.projectFileProvider = projectFileProvider;
    }

    public String execute(Project project) throws DomainException, IOException {
        projectFileProvider.create(project);
        return projectDatabaseProvider.create(project);
    }


}
