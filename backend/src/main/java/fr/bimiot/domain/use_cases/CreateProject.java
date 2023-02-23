package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.providers.ProjectDatabaseProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CreateProject {

    private final ProjectDatabaseProvider projectDatabaseProvider;

    public CreateProject(ProjectDatabaseProvider projectDatabaseProvider) {
        this.projectDatabaseProvider = projectDatabaseProvider;
    }

    public String execute(Project project) throws DomainException, IOException {
        return projectDatabaseProvider.create(project);
    }


}
