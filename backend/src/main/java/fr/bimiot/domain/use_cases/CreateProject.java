package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.providers.ProjectProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CreateProject {

    private final ProjectProvider projectProvider;

    public CreateProject(ProjectProvider projectProvider) {
        this.projectProvider = projectProvider;
    }

    public String execute(Project project) throws DomainException, IOException {
        return projectProvider.create(project);
    }


}
