package fr.bimiot.dataproviders.database;

import fr.bimiot.dataproviders.exception.DataBaseException;
import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.entities.SensorColor;
import fr.bimiot.domain.entities.SensorType;
import fr.bimiot.domain.use_cases.providers.ProjectProvider;
import fr.bimiot.utils.Builder;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProjectDatabaseProvider implements ProjectProvider {

    private final ProjectJpaRepository projectJpaRepository;


    public ProjectDatabaseProvider(ProjectJpaRepository projectJpaRepository) {
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
    public Project updateSensorsColorsByProjectName(String projectName, Map<SensorType, List<SensorColor>> sensorTypeListMap) throws DataBaseException {
        var projectExisted = projectJpaRepository.findProjectJpaByName(projectName);
        if (projectExisted == null) {
            throw new DataBaseException("Project doesn't exist", "404");
        }
        projectExisted.setSensorColorJpaMap(toSensorColorJpaMap(sensorTypeListMap));
        return toProject(projectJpaRepository.save(projectExisted));
    }

    @Override
    public Map<SensorType, List<SensorColor>> findSensorColorMapByProjectName(String projectName) {
        return toSensorColorMap(projectJpaRepository.findProjectJpaByName(projectName).getSensorColorJpaMap());
    }

    @Override
    public List<Project> getAllProjects() {
        return projectJpaRepository.findAll().stream()
                .map(this::toProject)
                .toList();
    }

    private Project toProject(ProjectJpa projectJpa) {
        return Builder.of(Project::new)
                .with(Project::setId, projectJpa.getId())
                .with(Project::setName, projectJpa.getName())
                .with(Project::setIfcFilename, projectJpa.getIfcFilename())
                .with(Project::setDatasetFilename, projectJpa.getDatasetFilename())
                .with(Project::setIfcFile, projectJpa.getIfc().getData())
                .with(Project::setSensorColors, toSensorColorMap(projectJpa.getSensorColorJpaMap()))
                .build();
    }

    @Override
    public byte[] loadIFCFile(String projectName) {
        return projectJpaRepository.findProjectJpaByName(projectName).getIfc().getData();
    }

    private List<SensorColorJpa> toSensorColorJpaList(List<SensorColor> sensorColors) {
        return sensorColors.stream()
                .map(sensorColor -> new SensorColorJpa(
                        sensorColor.colorCode(),
                        sensorColor.threshold()
                )).toList();
    }

    private Map<SensorTypeJpa, List<SensorColorJpa>> toSensorColorJpaMap(Map<SensorType, List<SensorColor>> sensorTypeListMap) {
        return sensorTypeListMap.entrySet().stream()
                .collect(Collectors.toMap(entry -> SensorTypeJpa.valueOf(entry.getKey().name()), entry -> toSensorColorJpaList(entry.getValue())));
    }

    private Map<SensorType, List<SensorColor>> toSensorColorMap(Map<SensorTypeJpa, List<SensorColorJpa>> sensorColorJpaMap) {
        return sensorColorJpaMap.entrySet().stream()
                .collect(Collectors.toMap(entry -> SensorType.valueOf(entry.getKey().name()), entry -> toSensorColorList(entry.getValue())));
    }

    private List<SensorColor> toSensorColorList(List<SensorColorJpa> sensorColorJpas) {
        return sensorColorJpas.stream()
                .map(sensorColorJpa -> new SensorColor(
                        sensorColorJpa.getColorCode(),
                        sensorColorJpa.getThreshold())).toList();
    }

    private ProjectJpa toProjectJpa(Project project) throws IOException {
        return Builder.of(ProjectJpa::new)
                .with(ProjectJpa::setIfc, toBinary(project.getIfcFile()))
                .with(ProjectJpa::setName, project.getName())
                .with(ProjectJpa::setIfcFilename, project.getIfcFilename())
                .with(ProjectJpa::setDatasetFilename, project.getDatasetFilename())
                .with(ProjectJpa::setSensorColorJpaMap, getDefaultSensorsColors())
                .build();
    }

    private Map<SensorTypeJpa, List<SensorColorJpa>> getDefaultSensorsColors() {
        Map<SensorTypeJpa, List<SensorColorJpa>> map = new EnumMap<>(SensorTypeJpa.class);
        map.put(SensorTypeJpa.TEMPERATURE, getDefaultTemperatureSensorColor());
        map.put(SensorTypeJpa.HUMIDITY, getDefaultHumiditySensorColor());
        map.put(SensorTypeJpa.LIGHT, getDefaultLightSensorColor());
        map.put(SensorTypeJpa.CO2, getDefaultCo2SensorColor());
        return map;
    }

    private List<SensorColorJpa> getDefaultTemperatureSensorColor() {
        return List.of(
                new SensorColorJpa("#7F00FF", -10f),
                new SensorColorJpa("#00FFFF", 20f),
                new SensorColorJpa("#80FF00", 30f),
                new SensorColorJpa("#FE0000", Float.POSITIVE_INFINITY)
        );
    }

    private List<SensorColorJpa> getDefaultHumiditySensorColor() {
        return List.of(
                new SensorColorJpa("#FE0000", 1f),
                new SensorColorJpa("#80FF00", 40f),
                new SensorColorJpa("#00FFFF", 70f),
                new SensorColorJpa("#0000FF", Float.POSITIVE_INFINITY)
        );
    }

    private List<SensorColorJpa> getDefaultLightSensorColor() {
        return List.of(
                new SensorColorJpa("#000000", 1f),
                new SensorColorJpa("#6D6D00", 30f),
                new SensorColorJpa("#B6B600", 60f),
                new SensorColorJpa("#FFFF00", Float.POSITIVE_INFINITY)
        );
    }

    private List<SensorColorJpa> getDefaultCo2SensorColor() {
        return List.of(
                new SensorColorJpa("#CFFF2E", 1100f),
                new SensorColorJpa("#FFE72E", 1400f),
                new SensorColorJpa("#FF9F2E", 5000f),
                new SensorColorJpa("#FF582E", Float.POSITIVE_INFINITY)
        );
    }

    private Binary toBinary(byte[] file) throws IOException {
        return new Binary(BsonBinarySubType.BINARY, file);
    }

    @Override
    public boolean isExistedProject(String projectName) {
        var existedProject = projectJpaRepository.findProjectJpaByName(projectName);
        return existedProject != null;
    }
}
