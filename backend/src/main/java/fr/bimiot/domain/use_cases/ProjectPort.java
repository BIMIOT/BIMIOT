package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.entities.Project;

public interface ProjectPort {
    void insert(Project project);
}
