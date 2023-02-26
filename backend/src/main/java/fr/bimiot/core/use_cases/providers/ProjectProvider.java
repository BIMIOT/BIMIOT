package fr.bimiot.core.use_cases.providers;

import fr.bimiot.core.entities.Project;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProjectProvider {
    Project save(Project project) throws IOException;

    void deleteById(String id);

    Optional<Project> findById(String id);

    List<Project> findAll();
}
