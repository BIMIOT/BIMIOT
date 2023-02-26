package fr.bimiot.core.use_cases;

import fr.bimiot.core.use_cases.providers.ProjectProvider;
import org.springframework.stereotype.Component;

@Component
public class DeleteProject {

    private final ProjectProvider projectProvider;

    public DeleteProject(ProjectProvider projectProvider) {
        this.projectProvider = projectProvider;
    }

    /**
     * Delete project in database
     * @param id of project
     */
    public void execute(String id) {
        projectProvider.deleteById(id);
    }
}
