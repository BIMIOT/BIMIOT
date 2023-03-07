package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.entities.SensorColor;
import fr.bimiot.domain.entities.SensorType;
import fr.bimiot.domain.use_cases.providers.ProjectProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GetSensorColorMap {

    private final ProjectProvider projectProvider;

    public GetSensorColorMap(ProjectProvider projectProvider) {
        this.projectProvider = projectProvider;
    }

    public Map<SensorType, List<SensorColor>> execute(String projectName) {
        return projectProvider.findSensorColorMapByProjectName(projectName);
    }
}
