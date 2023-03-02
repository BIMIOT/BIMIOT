package fr.bimiot.fixtures;

import fr.bimiot.domain.entities.Project;
import fr.bimiot.utils.Builder;

public class ProjectFixture {
    public static Project aProjectWithoutSensorsAndWithoutId() {
        return Builder.of(Project::new)
                .with(Project::setName, "Project X")
                .with(Project::setIfcFile, "Hello world".getBytes())
                .with(Project::setIfcFilename, "file.ifc")
                .with(Project::setDatasetFilename, "file.json")
                .build();
    }

    public static Project aCompleteProject() {
        return Builder.of(() -> aProjectWithoutSensorsAndWithoutId())
                .with(Project::setId, "ProjectID")
                .with(Project::setSensorColors, SensorColorMapFixture.sensorTypeListMapDomain())
                .build();
    }


}
