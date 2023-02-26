package fr.bimiot.core.use_cases;

import fr.bimiot.core.entities.Project;
import fr.bimiot.core.use_cases.providers.ProjectProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CreateProject {

    private final ProjectProvider projectProvider;

    public CreateProject(ProjectProvider projectProvider) {
        this.projectProvider = projectProvider;
    }

    /**
     * Create project in database
     *
     * @param project has to save
     * @return project's id
     * @throws IOException throw if there is problem with getting bytes content file(s)
     */
    public Project execute(Project project) throws IOException {
        return projectProvider.save(project);
    }


}
