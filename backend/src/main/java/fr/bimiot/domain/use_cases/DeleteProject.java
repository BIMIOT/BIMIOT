package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.providers.ProjectFileProvider;
import org.springframework.stereotype.Component;

@Component
public class DeleteProject {

    private final ProjectFileProvider projectFileProvider;

    public DeleteProject(ProjectFileProvider projectFileProvider) {
        this.projectFileProvider = projectFileProvider;
    }

    public void execute(String projectName) throws DomainException {
        projectFileProvider.delete(projectName);
    }

}
