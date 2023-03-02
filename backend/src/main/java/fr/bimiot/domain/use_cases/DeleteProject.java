package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.providers.ProjectProvider;
import org.springframework.stereotype.Component;

@Component
public class DeleteProject {

    private final ProjectProvider projectProvider;

    public DeleteProject(ProjectProvider projectProvider) {
        this.projectProvider = projectProvider;
    }

    public void execute(String projectName) throws DomainException {
        projectProvider.delete(projectName);
    }
}
