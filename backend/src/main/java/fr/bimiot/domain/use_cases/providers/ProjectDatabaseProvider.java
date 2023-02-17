package fr.bimiot.domain.use_cases.providers;

import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.exception.DomainException;

import java.io.IOException;

public interface ProjectDatabaseProvider {
    String create(Project project) throws IOException;

    void delete(String projectName);

}
