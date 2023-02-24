package fr.bimiot.domain.use_cases;

import fr.bimiot.dataproviders.exception.DataBaseException;
import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.entities.SensorColor;
import fr.bimiot.domain.entities.SensorType;
import fr.bimiot.domain.use_cases.providers.ProjectDatabaseProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class UpdateSensorsColors {
    private final ProjectDatabaseProvider projectDatabaseProvider;

    public UpdateSensorsColors(ProjectDatabaseProvider projectDatabaseProvider) {
        this.projectDatabaseProvider = projectDatabaseProvider;
    }

    /**
     * Update sensors colors of project name
     * @param projectName of project
     * @param sensorTypeListMap updated sensors colors
     * @return project with updated sensors colors
     * @throws DataBaseException throw this exception if project doesn't exist
     */
    public Project execute(String projectName, Map<SensorType, List<SensorColor>> sensorTypeListMap) throws DataBaseException {
        return projectDatabaseProvider.updateSensorsColorsByProjectName(projectName, sensorTypeListMap);
    }
}
