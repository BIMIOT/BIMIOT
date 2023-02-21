package fr.bimiot.application.controllers;

import fr.bimiot.application.ProjectApi;
import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.DeleteProject;
import fr.bimiot.domain.use_cases.ManageSimulation;
import fr.bimiot.domain.use_cases.projects.CreateProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/bimiot/projects")
public class ProjectController {
    private final CreateProject createProject;
    private final DeleteProject deleteProject;
    private final ManageSimulation manageSimulation;

    public ProjectController(CreateProject createProject, DeleteProject deleteProject, ManageSimulation manageSimulation) {
        this.createProject = createProject;
        this.deleteProject = deleteProject;
        this.manageSimulation = manageSimulation;
    }

    @PostMapping
    public ResponseEntity<ProjectApi> create(@RequestParam("name") String projectName, @RequestParam("ifc") MultipartFile ifc, @RequestParam("dataset") MultipartFile dataset) throws DomainException, IOException {
        manageSimulation.executeCreate(projectName, dataset);
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
        manageSimulation.executeDelete(projectName);
        deleteProject.execute(projectName);
        return ResponseEntity.status(HttpStatus.OK).body("The project is deleted");
    }
}
