package fr.bimiot.core.use_cases;

import fr.bimiot.core.entities.Project;
import fr.bimiot.core.exception.DomainException;
import fr.bimiot.core.use_cases.providers.ProjectProvider;
import org.springframework.stereotype.Component;

@Component
public class FindProjectById {
    private final ProjectProvider projectProvider;

    public FindProjectById(ProjectProvider projectProvider) {
        this.projectProvider = projectProvider;
    }

    public Project execute(String id) throws DomainException {
        return projectProvider.findById(id).orElseThrow(() -> new DomainException("Project doesn't exist"));
    }
}
