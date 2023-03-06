package fr.bimiot.application.controllers;

import fr.bimiot.application.dtos.*;
import fr.bimiot.dataproviders.exception.DataBaseException;
import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.entities.SensorColor;
import fr.bimiot.domain.entities.SensorType;
import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.*;
import fr.bimiot.utils.Builder;
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
    private final ManageSimulation manageSimulation;
    private final ManageData manageData;
    private final UpdateSensorsColors updateSensorsColors;
    private final GetSensorColorMap getSensorColorMap;

    public ProjectController(CreateProject createProject, DeleteProject deleteProject, ManageSimulation manageSimulation, ManageData manageData, UpdateSensorsColors updateSensorsColors, GetSensorColorMap getSensorColorMap) {
        this.createProject = createProject;
        this.deleteProject = deleteProject;
        this.manageSimulation = manageSimulation;
        this.manageData = manageData;
        this.updateSensorsColors = updateSensorsColors;
        this.getSensorColorMap = getSensorColorMap;
    }

    @PostMapping
    public ResponseEntity<ProjectApi> create(@RequestParam("name") String projectName, @RequestParam("ifc") MultipartFile ifc, @RequestParam("dataset") MultipartFile dataset) throws DomainException, IOException {
        var projectResponse = createProject.execute(toProject(projectName, ifc, dataset));
        manageSimulation.executeCreate(projectName, dataset);
        return ResponseEntity.ok(toProjectApi(projectResponse));
    }

    private Project toProject(String projectName, MultipartFile ifc, MultipartFile dataset) throws IOException {
        return Builder.of(Project::new)
                .with(Project::setName, projectName)
                .with(Project::setIfcFile, ifc.getBytes())
                .with(Project::setIfcFilename, ifc.getOriginalFilename())
                .with(Project::setDatasetFilename, dataset.getOriginalFilename())
                .build();
    }

    private ProjectApi toProjectApi(String projectId) {
        var projectApi = new ProjectApi();
        projectApi.setId(projectId);
        return projectApi;
    }

    @DeleteMapping("/{projectName}")
    public ResponseEntity<String> deleteProject(@PathVariable("projectName") String projectName) throws DomainException {
        manageSimulation.executeDelete(projectName);
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
