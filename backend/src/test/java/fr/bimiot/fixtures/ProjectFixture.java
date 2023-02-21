package fr.bimiot.fixtures;

import fr.bimiot.domain.entities.Project;
import org.springframework.mock.web.MockMultipartFile;

public class ProjectFixture {
    public static Project aProjectWithoutSensorsAndWithoutId(){
        var project = new Project();
        project.setName("Project X");
        project.setIfc(new MockMultipartFile("file.ifc", "Hello world".getBytes()));
        project.setDataset(new MockMultipartFile("file.json", "Hello world".getBytes()));
        return project;
    }

    public static Project aProjectWithOnlyTemperatureSensor(){
        var project = aProjectWithoutSensorsAndWithoutId();
        project.setId("ProjectID");
        project.setSensorColors(SensorColorMapFixture.sensorTypeListMapDomain());
        return project;
    }


}
