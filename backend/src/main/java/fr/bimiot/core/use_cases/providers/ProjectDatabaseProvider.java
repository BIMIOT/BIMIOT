package fr.bimiot.core.use_cases.providers;

import fr.bimiot.dataproviders.exception.DataBaseException;
import fr.bimiot.core.entities.Project;
import fr.bimiot.core.entities.SensorColor;
import fr.bimiot.core.entities.SensorType;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ProjectDatabaseProvider {
    String create(Project project) throws IOException;

    void delete(String projectName);
    Project updateSensorsColorsByProjectName(String projectName, Map<SensorType, List<SensorColor>> sensorTypeListMap) throws DataBaseException;

    Map<SensorType, List<SensorColor>> findSensorColorMapByProjectName(String projectName);

    List<String> getAllProjects();

    byte[] loadIFCFile(String projectName);
}
