package fr.bimiot.fixtures;

import fr.bimiot.core.entities.Project;

public class ProjectFixture {
    public static Project aProjectWithoutSensorsAndWithoutId(){
        var project = new Project();
        project.setName("Project X");
        project.setIfc("file.ifc".getBytes());
        project.setDataset("dataset.json".getBytes());
        return project;
    }

    public static Project aCompleteProject(){
        var project = aProjectWithoutSensorsAndWithoutId();
        project.setId("ProjectID");
        project.setSensorColors(SensorColorMapFixture.sensorTypeListMapDomain());
        return project;
    }


}
