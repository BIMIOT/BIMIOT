package fr.bimiot.application;

import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.CreateProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/bimiot/projects")
public class ProjectController {

    private final CreateProject createProject;

    @Autowired
    public ProjectController(CreateProject createProject) {
        this.createProject = createProject;
    }

    @PostMapping("/folder")
    public ResponseEntity<ProjectDirectoryApi> createProject(@RequestBody ProjectDirectoryApi projectDirectoryApi) throws DomainException {
        createProject.createFolder(toProjectDirectory(projectDirectoryApi));
        return ResponseEntity.status(HttpStatus.OK).body(projectDirectoryApi);
    }

    @PostMapping("/files/{projectName}")
    public ResponseEntity<List<String>> uploadProjectFiles(@RequestParam("files") MultipartFile[] files, @PathVariable("projectName") String projectName) throws DomainException {
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            createProject.uploadProjectFile(projectName, file);
            fileNames.add(file.getOriginalFilename());
        }
        return ResponseEntity.status(HttpStatus.OK).body(fileNames);
    }

    private Project toProjectDirectory(ProjectDirectoryApi projectDirectoryApi) {
        return new Project(projectDirectoryApi.getProjectName());
    }

    public ResponseEntity<List<String>> deleteProject(@PathVariable("projectName") String projectName){

    }

}
