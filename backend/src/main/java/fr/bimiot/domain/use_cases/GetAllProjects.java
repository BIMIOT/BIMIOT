package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.use_cases.providers.ProjectProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetAllProjects {
    private final ProjectProvider projectProvider;

    public GetAllProjects(ProjectProvider projectProvider) {
        this.projectProvider = projectProvider;
    }

    public List<Project> execute() {
        return projectProvider.getAllProjects();
    }
}
