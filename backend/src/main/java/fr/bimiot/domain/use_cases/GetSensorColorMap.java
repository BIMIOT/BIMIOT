package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.entities.SensorColor;
import fr.bimiot.domain.entities.SensorType;
import fr.bimiot.domain.use_cases.providers.ProjectDatabaseProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GetSensorColorMap {

    private final ProjectDatabaseProvider projectDatabaseProvider;

    public GetSensorColorMap(ProjectDatabaseProvider projectDatabaseProvider) {
        this.projectDatabaseProvider = projectDatabaseProvider;
    }

    /**
     * Get sensors with colors for each sensor and threshold for each color
     * @param projectName of corresponding project
     * @return all type of sensor with colors and threshold for each
     */
    public Map<SensorType, List<SensorColor>> execute(String projectName) {
        return projectDatabaseProvider.findSensorColorMapByProjectName(projectName);
    }
}
