package fr.bimiot.domain.use_cases.providers;

import fr.bimiot.dataproviders.exception.DataBaseException;
import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.entities.TypesColors;

import java.io.IOException;

public interface ProjectDatabaseProvider {
    String create(Project project) throws IOException;

    void delete(String projectName);
    Project updateSensorsColorsByProjectName(String projectName, TypesColors typesColors) throws DataBaseException;

}
