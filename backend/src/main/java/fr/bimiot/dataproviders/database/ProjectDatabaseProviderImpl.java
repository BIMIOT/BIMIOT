package fr.bimiot.dataproviders.database;

import fr.bimiot.dataproviders.exception.DataBaseException;
import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.entities.SensorColor;
import fr.bimiot.domain.entities.SensorType;
import fr.bimiot.domain.use_cases.providers.ProjectDatabaseProvider;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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
    public Project updateSensorsColorsByProjectName(String projectName, Map<SensorType, List<SensorColor>> sensorTypeListMap) throws DataBaseException {
        var projectExisted = projectJpaRepository.findProjectJpaByName(projectName);
        if (projectExisted == null) {
            throw new DataBaseException("Project doesn't exist", "404");
        }
        projectExisted.setSensorColorJpaMap(toSensorColorJpaMap(sensorTypeListMap));
        return toProject(projectJpaRepository.save(projectExisted));
    }

    private List<SensorColorJpa> toSensorColorJpaList(List<SensorColor> sensorColors) {
        return sensorColors.stream()
                .map(sensorColor -> new SensorColorJpa(
                        sensorColor.colorCode(),
                        sensorColor.threshold()
                )).collect(Collectors.toList());
    }

    private Project toProject(ProjectJpa projectJpa) {
        var project = new Project();
        project.setId(projectJpa.getId());
        project.setName(projectJpa.getName());
        project.setSensorColors(toSensorColorMap(projectJpa.getSensorColorJpaMap()));
        return project;
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
                        sensorColorJpa.getThreshold())).collect(Collectors.toList());
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
}
