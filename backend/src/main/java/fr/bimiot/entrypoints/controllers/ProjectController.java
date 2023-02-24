package fr.bimiot.entrypoints.controllers;

import fr.bimiot.entrypoints.dtos.ProjectApi;
import fr.bimiot.entrypoints.dtos.SensorColorApi;
import fr.bimiot.entrypoints.dtos.SensorColorApiMap;
import fr.bimiot.entrypoints.dtos.SensorTypeApi;
import fr.bimiot.dataproviders.exception.DataBaseException;
import fr.bimiot.core.entities.Project;
import fr.bimiot.core.entities.SensorColor;
import fr.bimiot.core.entities.SensorType;
import fr.bimiot.core.use_cases.*;
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
    private final GetAllProjects getAllProjects;

    public ProjectController(CreateProject createProject, DeleteProject deleteProject, ManageSimulation manageSimulation, ManageData manageData, UpdateSensorsColors updateSensorsColors, GetSensorColorMap getSensorColorMap, GetAllProjects getAllProjects) {
        this.createProject = createProject;
        this.deleteProject = deleteProject;
        this.manageSimulation = manageSimulation;
        this.manageData = manageData;
        this.updateSensorsColors = updateSensorsColors;
        this.getSensorColorMap = getSensorColorMap;
        this.getAllProjects = getAllProjects;
    }

    /**
     * Create new project in database and create new simulation in simulator with dataset file
     * @param projectName of new project
     * @param ifc data structure file
     * @param dataset json dataset file
     * @return project created
     * @throws IOException throw if there is problem with getting content bytes of file(s)
     */
    @PostMapping
    public ResponseEntity<ProjectApi> create(@RequestParam("name") String projectName, @RequestParam("ifc") MultipartFile ifc, @RequestParam("dataset") MultipartFile dataset) throws IOException {
        manageSimulation.executeCreate(projectName, dataset);
        var projectResponse = createProject.execute(toProject(projectName, ifc, dataset));
        return ResponseEntity.ok(toProjectApi(projectResponse));
    }

    /**
     * Get all project names
     * @return list of existed project names
     */
    @GetMapping
    public ResponseEntity<List<String>> getAllProjects() {
        return ResponseEntity.status(HttpStatus.OK).body(getAllProjects.execute());
    }

    private Project toProject(String projectName, MultipartFile ifc, MultipartFile dataset) {
        return new Project(null, projectName, ifc, dataset);
    }

    private ProjectApi toProjectApi(String projectId) {
        var projectApi = new ProjectApi();
        projectApi.setId(projectId);
        return projectApi;
    }

    /**
     * Delete project in database and in simulator
     * @param projectName of corresponding project
     */
    @DeleteMapping("/{projectName}")
    public ResponseEntity<String> deleteProject(@PathVariable("projectName") String projectName) {
        manageSimulation.executeDelete(projectName);
        deleteProject.execute(projectName);
        return ResponseEntity.status(HttpStatus.OK).body("The project is deleted");
    }

    /**
     * Update the colors of the sensors of a project
     * @param projectName of the project
     * @param sensorColorApiMap of the project contains all type of sensor and for each its colors with threshold
     * @return the corresponding project with new sensors colors
     * @throws DataBaseException throw this exception if project doesn't exist
     */
    @PutMapping("/colors/{projectName}")
    public ResponseEntity<ProjectApi> updateProjectColors(@PathVariable("projectName") String projectName, @RequestBody SensorColorApiMap sensorColorApiMap) throws DataBaseException {
        var project = updateSensorsColors.execute(projectName, toSensorColorMap(sensorColorApiMap));
        manageData.setSensorTypeListMap(project.getSensorColors());
        return ResponseEntity.status(HttpStatus.OK).body(toProjectApi(project));
    }

    /**
     * Get sensors colors of a project
     * @param projectName of corresponding project
     * @return map of all color types with colors and threshold for each type
     */
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
                .collect(Collectors.toMap(entry -> SensorType.valueOf(entry.getKey()), entry -> toSensorColorList(entry.getValue())));
    }

    private List<SensorColor> toSensorColorList(List<SensorColorApi> sensorColorApis) {
        return sensorColorApis.stream()
                .map(sensorColorApi -> new SensorColor(
                        sensorColorApi.getColorCode(),
                        sensorColorApi.getThreshold() == null ? Float.POSITIVE_INFINITY : sensorColorApi.getThreshold())).toList();
    }
}
