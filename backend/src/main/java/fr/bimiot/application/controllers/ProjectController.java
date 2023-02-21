package fr.bimiot.application.controllers;

import fr.bimiot.application.dtos.ProjectApi;
import fr.bimiot.application.dtos.SensorColorApi;
import fr.bimiot.application.dtos.SensorColorApiMap;
import fr.bimiot.application.dtos.SensorTypeApi;
import fr.bimiot.dataproviders.exception.DataBaseException;
import fr.bimiot.domain.entities.*;
import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.CreateProject;
import fr.bimiot.domain.use_cases.DeleteProject;
import fr.bimiot.domain.use_cases.UpdateSensorsColors;
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
    private final UpdateSensorsColors updateSensorsColors;

    public ProjectController(CreateProject createProject, DeleteProject deleteProject, UpdateSensorsColors updateSensorsColors) {
        this.createProject = createProject;
        this.deleteProject = deleteProject;
        this.updateSensorsColors = updateSensorsColors;
    }

    @PostMapping
    public ResponseEntity<ProjectApi> create(@RequestParam("name") String projectName, @RequestParam("ifc") MultipartFile ifc, @RequestParam("dataset") MultipartFile dataset) throws DomainException, IOException {
        var projectResponse = createProject.execute(toProject(projectName, ifc, dataset));
        return ResponseEntity.ok(toProjectApi(projectResponse));
    }

    private Project toProject(String projectName, MultipartFile ifc, MultipartFile dataset) {
        return new Project(null, projectName, ifc, dataset);
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
        return ResponseEntity.status(HttpStatus.OK).body(toProjectApi(updateSensorsColors.execute(projectName, toSensorColorMap(sensorColorApiMap))));
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
                        sensorColor.threshold()))
                .collect(Collectors.toList());
    }

    private Map<SensorType, List<SensorColor>> toSensorColorMap(SensorColorApiMap sensorColorApiMap) {
        return sensorColorApiMap.getSensorColorApis().entrySet().stream()
                .collect(Collectors.toMap(entry -> SensorType.valueOf(entry.getKey()), entry -> toSensorColorList(entry.getValue())));
    }

    private List<SensorColor> toSensorColorList(List<SensorColorApi> sensorColorApis) {
        return sensorColorApis.stream()
                .map(sensorColorApi -> new SensorColor(
                        sensorColorApi.getColorCode(),
                        sensorColorApi.getThreshold() == null ? Float.POSITIVE_INFINITY : sensorColorApi.getThreshold()))
                .collect(Collectors.toList());
    }
}
