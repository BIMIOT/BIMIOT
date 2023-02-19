package fr.bimiot.dataproviders.database;

import fr.bimiot.dataproviders.exception.DataBaseException;
import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.entities.SensorType;
import fr.bimiot.domain.entities.TypeColor;
import fr.bimiot.domain.entities.TypesColors;
import fr.bimiot.domain.use_cases.providers.ProjectDatabaseProvider;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class ProjectDatabaseProviderImpl implements ProjectDatabaseProvider {

    private final ProjectJpaRepository projectJpaRepository;


    public ProjectDatabaseProviderImpl(ProjectJpaRepository projectJpaRepository) {
        this.projectJpaRepository = projectJpaRepository;
    }

    @Override
    public String create(Project project) throws IOException {
        return projectJpaRepository.insert(toProjectJpa(project)).getId();
    }

    @Override
    public void delete(String projectName) {
        projectJpaRepository.deleteByName(projectName);
    }

    @Override
    public Project updateSensorsColorsByProjectName(String projectName, TypesColors typesColors) throws DataBaseException {
        var projectExisted = projectJpaRepository.findProjectJpaByName(projectName);
        if (projectExisted == null) {
            throw new DataBaseException("Project doesn't exist", "404");
        }
        projectExisted.setSensorsColorsJpa(toSensorsColorsJpa(typesColors));
        return toProject(projectJpaRepository.save(projectExisted));
    }

    private Project toProject(ProjectJpa projectJpa) {
        var project = new Project();
        project.setId(projectJpa.getId());
        project.setName(projectJpa.getName());
        project.setTypesColors(toTypesColors(projectJpa.getSensorsColorsJpa()));
        return project;
    }

    private ProjectJpa toProjectJpa(Project project) throws IOException {
        ProjectJpa projectJpa = new ProjectJpa();
        projectJpa.setIfc(toBinary(project.getIfc()));
        projectJpa.setDataset(toBinary(project.getDataset()));
        projectJpa.setName(project.getName());
        return projectJpa;
    }

    private Binary toBinary(MultipartFile file) throws IOException {
        return new Binary(BsonBinarySubType.BINARY, file.getBytes());
    }

    private SensorsColorsJpa toSensorsColorsJpa(TypesColors typesColors) {
        var sensorsColorsJpa = new SensorsColorsJpa();
        var newMap = typesColors.getTypesColor().entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().name(), entry -> toSensorColorsJpa(entry.getValue())));
        sensorsColorsJpa.setSensorsColorsJpa(newMap);
        return sensorsColorsJpa;
    }

    private SensorColorsJpa toSensorColorsJpa(TypeColor typeColor) {
        var sensorColorsJpa = new SensorColorsJpa();
        sensorColorsJpa.setColors(typeColor.getColors());
        sensorColorsJpa.setValues(typeColor.getValues());
        return sensorColorsJpa;
    }

    private TypesColors toTypesColors(SensorsColorsJpa sensorsColorsJpa) {
        var typesColors = new TypesColors();
        var map = sensorsColorsJpa.getSensorsColorsJpa().entrySet().stream()
                .collect(Collectors.toMap(entry -> SensorType.valueOf(entry.getKey()), entry -> toTypeColor(entry.getValue())));
        typesColors.setTypesColor(map);
        return typesColors;
    }

    private TypeColor toTypeColor(SensorColorsJpa sensorColorsJpa) {
        var typeColor = new TypeColor();
        typeColor.setColors(sensorColorsJpa.getColors());
        typeColor.setValues(sensorColorsJpa.getValues());
        return typeColor;
    }
}
