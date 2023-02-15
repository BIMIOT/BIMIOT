package fr.bimiot.domain.use_cases.providers;

import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.exception.DomainException;

import java.io.IOException;

public interface ProjectFileProvider {
    void create(Project project) throws DomainException, IOException;

    void delete(String projectName) throws DomainException;
}
