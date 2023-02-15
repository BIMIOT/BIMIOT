package fr.bimiot.application.controllers;

import fr.bimiot.application.ProjectApi;
import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.projects.CreateProject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/bimiot/projects")
public class ProjectController {

    private final CreateProject createProject;

    public ProjectController(CreateProject createProject) {
        this.createProject = createProject;
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
}
