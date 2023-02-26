package fr.bimiot.entrypoints.rest;

import fr.bimiot.core.entities.Project;
import fr.bimiot.core.entities.SensorColor;
import fr.bimiot.core.entities.SensorType;
import fr.bimiot.core.use_cases.*;
import fr.bimiot.entrypoints.dtos.ProjectApi;
import fr.bimiot.entrypoints.dtos.SensorColorApi;
import fr.bimiot.entrypoints.dtos.SensorTypeApi;
import fr.bimiot.entrypoints.dtos.UpdateProjectRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bimiot/projects")
public class ProjectEndpoint {
    private final CreateProject createProject;
    private final DeleteProject deleteProject;
    private final ManageSimulation manageSimulation;
    private final ManageData manageData;
    private final UpdateProject updateProject;
    private final FindAllProjects findAllProjects;

    public ProjectEndpoint(CreateProject createProject, DeleteProject deleteProject, ManageSimulation manageSimulation, ManageData manageData, UpdateProject updateProject, FindAllProjects findAllProjects) {
        this.createProject = createProject;
        this.deleteProject = deleteProject;
        this.manageSimulation = manageSimulation;
        this.manageData = manageData;
        this.updateProject = updateProject;
        this.findAllProjects = findAllProjects;
    }

    /**
     * Create new project in database and save new simulation in simulator with dataset file
     *
     * @param projectName of new project
     * @param ifc         data structure file
     * @param dataset     json dataset file
     * @return project created
     * @throws IOException throw if there is problem with getting content bytes of file(s)
     */
    @PostMapping
    public ResponseEntity<ProjectApi> create(@RequestParam("name") String projectName, @RequestParam("ifc") MultipartFile ifc, @RequestParam("dataset") MultipartFile dataset) throws IOException {
        manageSimulation.executeCreate(projectName, dataset);
        var projectApi = new ProjectApi(null, projectName, ifc.getBytes(), null);
        var projectResponse = createProject.execute(toProject(projectApi));
        return ResponseEntity.ok(toProjectApi(projectResponse));
    }

    /**
     * Get all project names
     *
     * @return list of existed project names
     */
    @GetMapping
    public ResponseEntity<List<ProjectApi>> findAllProjects() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(findAllProjects.execute().stream()
                        .map(this::toProjectApi)
                        .toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectApi> updateProject(@PathVariable String id, @RequestBody UpdateProjectRequest updateProjectRequest) throws IOException {
        var projectApi = new ProjectApi(
                id,
                updateProjectRequest.getName(),
                updateProjectRequest.getIfcFileContent(),
                updateProjectRequest.getSensorsColorsMap()
        );
        return ResponseEntity.status(HttpStatus.OK).body(toProjectApi(updateProject.execute(toProject(projectApi))));
    }

    private Project toProject(ProjectApi projectApi) {
        var project = new Project();
        project.setId(projectApi.getId());
        project.setName(projectApi.getName());
        project.setIfc(projectApi.getIfcFileContent());
        project.setSensorColors(toCoreMap(projectApi.getSensorsColorsApi()));
        return project;
    }

    private Map<SensorType, List<SensorColor>> toCoreMap(Map<SensorTypeApi, List<SensorColorApi>> entryPointMap) {
        var map = new EnumMap<SensorType, List<SensorColor>>(SensorType.class);
        entryPointMap.entrySet()
                .forEach(entry -> {
                    var type = SensorType.valueOf(entry.getKey().name());
                    var list = entry.getValue().stream()
                            .map(sensorColorApi -> new SensorColor(sensorColorApi.getColorCode(), sensorColorApi.getThreshold()))
                            .toList();
                    map.put(type, list);
                });
        return map;
    }

    /**
     * Delete project in database and in simulator
     *
     * @param id of corresponding project
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable String id) {
        //manageSimulation.executeDelete(projectName);
        deleteProject.execute(id);
        return ResponseEntity.ok().build();
    }

    private ProjectApi toProjectApi(Project project) {
        return new ProjectApi(
                project.getId(),
                project.getName(),
                project.getIfc(),
                toApiMap(project.getSensorColors())
        );
    }

    private Map<SensorTypeApi, List<SensorColorApi>> toApiMap(Map<SensorType, List<SensorColor>> coreMap) {
        var apiMap = new EnumMap<SensorTypeApi, List<SensorColorApi>>(SensorTypeApi.class);
        coreMap.entrySet().forEach(entry -> {
            var type = SensorTypeApi.valueOf(entry.getKey().name());
            var list = entry.getValue().stream()
                    .map(sensorColor -> new SensorColorApi(sensorColor.colorCode(), sensorColor.threshold()))
                    .toList();
            apiMap.put(type, list);
        });
        return apiMap;
    }
}
