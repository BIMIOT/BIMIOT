package fr.bimiot.core.use_cases;

import fr.bimiot.core.entities.Project;
import fr.bimiot.core.use_cases.providers.ProjectProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FindAllProjects {
    private final ProjectProvider projectProvider;

    public FindAllProjects(ProjectProvider projectProvider) {
        this.projectProvider = projectProvider;
    }

    /**
     * Get all project's name
     * @return list of all project's names
     */
    public List<Project> execute() {
        return projectProvider.findAll();
    }
}
