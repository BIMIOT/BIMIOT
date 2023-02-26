package fr.bimiot.core.use_cases;

import fr.bimiot.core.entities.Project;
import fr.bimiot.core.use_cases.providers.ProjectProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UpdateProject {
    private final ProjectProvider projectProvider;

    public UpdateProject(ProjectProvider projectProvider) {
        this.projectProvider = projectProvider;
    }

    public Project execute(Project project) throws IOException {
        return projectProvider.save(project);
    }
}
