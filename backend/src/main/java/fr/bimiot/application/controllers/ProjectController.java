package fr.bimiot.application.controllers;

import fr.bimiot.application.ProjectApi;
import fr.bimiot.application.SensorColorsApi;
import fr.bimiot.application.SensorsColorsApi;
import fr.bimiot.dataproviders.exception.DataBaseException;
import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.entities.SensorType;
import fr.bimiot.domain.entities.TypeColor;
import fr.bimiot.domain.entities.TypesColors;
import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.CreateProject;
import fr.bimiot.domain.use_cases.DeleteProject;
import fr.bimiot.domain.use_cases.UpdateSensorsColors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public ResponseEntity<ProjectApi> updateProjectColors(@PathVariable("projectName") String projectName, @RequestBody SensorsColorsApi sensorsColorsApi) throws DataBaseException {
        return ResponseEntity.status(HttpStatus.OK).body(toProjectApi(updateSensorsColors.execute(projectName, toTypesColors(sensorsColorsApi))));
    }

    private ProjectApi toProjectApi(Project project) {
        var projectApi = new ProjectApi();
        projectApi.setId(project.getId());
        projectApi.setName(project.getName());
        projectApi.setSensorsColorsApi(toSensorsColorsApi(project.getTypesColors()));
        return projectApi;
    }

    private SensorsColorsApi toSensorsColorsApi(TypesColors typesColors) {
        var sensorsColorsApi = new SensorsColorsApi();
        var map = typesColors.getTypesColor().entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().name(), entry -> toSensorColorsApi(entry.getValue())));
        sensorsColorsApi.setSensorsColors(map);
        return sensorsColorsApi;
    }

    private SensorColorsApi toSensorColorsApi(TypeColor typeColor) {
        var sensorColorsApi = new SensorColorsApi();
        sensorColorsApi.setColors(typeColor.getColors());
        sensorColorsApi.setValues(typeColor.getValues());
        return sensorColorsApi;
    }

    private TypesColors toTypesColors(SensorsColorsApi sensorsColorsApi) {
        var typesColors = new TypesColors();
        var newMap = sensorsColorsApi.getSensorsColors().entrySet().stream()
                .collect(Collectors.toMap(entry -> SensorType.valueOf(entry.getKey()), entry -> toTypeColor(entry.getValue())));
        typesColors.setTypesColor(newMap);
        return typesColors;
    }

    private TypeColor toTypeColor(SensorColorsApi sensorColorsApi) {
        var typeColor = new TypeColor();
        typeColor.setColors(sensorColorsApi.getColors());
        typeColor.setValues(sensorColorsApi.getValues());
        return typeColor;
    }
}
