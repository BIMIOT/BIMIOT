package fr.bimiot.domain.use_cases.providers;

import fr.bimiot.dataproviders.exception.DataBaseException;
import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.entities.SensorColor;
import fr.bimiot.domain.entities.SensorType;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ProjectDatabaseProvider {
    String create(Project project) throws IOException;

    void delete(String projectName);
    Project updateSensorsColorsByProjectName(String projectName, Map<SensorType, List<SensorColor>> sensorTypeListMap) throws DataBaseException;

    Map<SensorType, List<SensorColor>> findSensorColorMapByProjectName(String projectName);

    List<String> getAllProjects();

    byte[] loadFile(String projectName);
}
