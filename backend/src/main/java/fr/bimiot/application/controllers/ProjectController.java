package fr.bimiot.application.controllers;

import fr.bimiot.application.mappers.ProjectMapper;
import fr.bimiot.application.dtos.*;
import fr.bimiot.dataproviders.exception.DataBaseException;
import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.entities.SensorColor;
import fr.bimiot.domain.entities.SensorType;
import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bimiot/projects")
public class ProjectController {
    private final CreateProject createProject;
    private final DeleteProject deleteProject;
    private final ManageData manageData;
    private final UpdateSensorsColors updateSensorsColors;
    private final GetSensorColorMap getSensorColorMap;
    private final GetAllProjects getAllProjects;

    public ProjectController(CreateProject createProject, DeleteProject deleteProject, ManageData manageData, UpdateSensorsColors updateSensorsColors, GetSensorColorMap getSensorColorMap, GetAllProjects getAllProjects) {
        this.createProject = createProject;
        this.deleteProject = deleteProject;
        this.manageData = manageData;
        this.updateSensorsColors = updateSensorsColors;
        this.getSensorColorMap = getSensorColorMap;
        this.getAllProjects = getAllProjects;
    }

    @GetMapping
    public ResponseEntity<List<ProjectApiGetAllResponse>> getAllProjects() {
        var responseBody = getAllProjects.execute().stream()
                .map(ProjectMapper::toProjectApiGetAllResponse)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @PostMapping
    public ResponseEntity<ProjectApi> create(@RequestParam("name") String projectName, @RequestParam("ifc") MultipartFile ifc, @RequestParam("dataset") MultipartFile dataset) throws DomainException, IOException {
        var projectResponse = createProject.execute(ProjectMapper.toProject(projectName, ifc, dataset), dataset);
        return ResponseEntity.status(HttpStatus.OK).body(toProjectApi(projectResponse));
    }

    private ProjectApi toProjectApi(String projectId) {
        var projectApi = new ProjectApi();
        projectApi.setId(projectId);
        return projectApi;
    }

    @DeleteMapping("/{projectName}")
    public ResponseEntity<String> deleteProject(@PathVariable("projectName") String projectName) throws DomainException {
        deleteProject.execute(projectName);
        return ResponseEntity.status(HttpStatus.OK).body("The project is deleted");
    }

    @PutMapping("/colors/{projectName}")
    public ResponseEntity<ProjectApi> updateProjectColors(@PathVariable("projectName") String projectName, @RequestBody SensorColorApiMap sensorColorApiMap) throws DataBaseException {
        var project = updateSensorsColors.execute(projectName, toSensorColorMap(sensorColorApiMap));
        manageData.setSensorTypeListMap(project.getSensorColors());
        manageData.sendAllRoomsColors();
        return ResponseEntity.status(HttpStatus.OK).body(toProjectApi(project));
    }

    @GetMapping("/colors/{projectName}")
    public ResponseEntity<Map<SensorTypeApi, List<SensorColorApi>>> getSensorColorMap(@PathVariable("projectName") String projectName) {
        var map = getSensorColorMap.execute(projectName);
        manageData.setSensorTypeListMap(map);
        return ResponseEntity.status(HttpStatus.OK).body(toSensorColorApiMap(map));
    }

    private ProjectApi toProjectApi(Project project) {
        var projectApi = new ProjectApi();
        projectApi.setId(project.getId());
        projectApi.setName(project.getName());
        projectApi.setSensorsColorsApi(toSensorColorApiMap(project.getSensorColors()));
        return projectApi;
    }

    private Map<SensorTypeApi, List<SensorColorApi>> toSensorColorApiMap(Map<SensorType, List<SensorColor>> sensorTypeListMap) {
        return sensorTypeListMap.entrySet().stream()
                .collect(Collectors.toMap(entry -> SensorTypeApi.valueOf(entry.getKey().name()), entry -> toSensorColorApiList(entry.getValue())));
    }

    private List<SensorColorApi> toSensorColorApiList(List<SensorColor> sensorColors) {
        return sensorColors.stream()
                .map(sensorColor -> new SensorColorApi(
                        sensorColor.colorCode(),
                        sensorColor.threshold())).toList();
    }

    private Map<SensorType, List<SensorColor>> toSensorColorMap(SensorColorApiMap sensorColorApiMap) {
        return sensorColorApiMap.getSensorColorApis().entrySet().stream()
                .collect(Collectors.toMap(entry -> SensorType.valueOf(entry.getKey().name()), entry -> toSensorColorList(entry.getValue())));
    }

    private List<SensorColor> toSensorColorList(List<SensorColorApi> sensorColorApis) {
        return sensorColorApis.stream()
                .map(sensorColorApi -> new SensorColor(
                        sensorColorApi.getColorCode(),
                        sensorColorApi.getThreshold() == null ? Float.POSITIVE_INFINITY : sensorColorApi.getThreshold())).toList();
    }
}
