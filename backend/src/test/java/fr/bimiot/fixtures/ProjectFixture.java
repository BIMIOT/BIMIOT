package fr.bimiot.fixtures;

import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.entities.SensorType;
import fr.bimiot.domain.entities.TypeColor;
import fr.bimiot.domain.entities.TypesColors;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Map;

public class ProjectFixture {
    public static Project aProjectForCreation(){
        var project = new Project();
        project.setName("Project X");
        project.setIfc(new MockMultipartFile("file.ifc", "Hello world".getBytes()));
        project.setIfc(new MockMultipartFile("file.json", "Hello world".getBytes()));
        return project;
    }

    public static Project aProjectWithOnlyTemperatureSensor(){
        var project = aProjectForCreation();
        project.setId("ProjectID");
        project.setTypesColors(getTypesColors());
        return project;
    }

    private static TypesColors getTypesColors(){
        var typesColors = new TypesColors();
        typesColors.setTypesColor(Map.of(SensorType.TEMPERATURE, getTypeColor()));
        return typesColors;
    }
    private static TypeColor getTypeColor(){
        var typeColor = new TypeColor();
        typeColor.setColors(List.of("#219ebc", "#ffb703"));
        typeColor.setValues(List.of(10f));
        return typeColor;
    }
}
