package fr.bimiot.application;

import fr.bimiot.domain.entities.Data;
import fr.bimiot.domain.entities.ProjectDirectory;
import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.CreateProject;
import fr.bimiot.domain.use_cases.GetAllProjects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/bimiot")
public class BimIotController {
    private final CreateProject createProjectUseCase;
    private final GetAllProjects getAllProjects;

    @Autowired
    public BimIotController(CreateProject createProject, GetAllProjects getAllProjects) {
        this.createProjectUseCase = createProject;
        this.getAllProjects = getAllProjects;
    }

    @PostMapping("/project/folder")
    public ResponseEntity<ProjectDirectoryApi> createProject(@RequestBody ProjectDirectoryApi projectDirectoryApi) throws DomainException {
        createProjectUseCase.createFolder(toProjectDirectory(projectDirectoryApi));
        return ResponseEntity.status(HttpStatus.OK).body(projectDirectoryApi);
    }

    @PostMapping("/project/files/{projectName}")
    public ResponseEntity<List<String>> uploadProjectFiles(@RequestParam("files") MultipartFile[] files, @PathVariable("projectName") String projectName) throws DomainException {
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            createProjectUseCase.uploadProjectFile(projectName, file);
            fileNames.add(file.getOriginalFilename());
        }
        return ResponseEntity.status(HttpStatus.OK).body(fileNames);
    }

    @GetMapping("/projects")
    public ResponseEntity<List<String>> getAllProjects() {
        return ResponseEntity.status(HttpStatus.OK).body(getAllProjects.execute());
    }

    @PutMapping(value = "/sendData", consumes = "application/json")
    public void sendData(@RequestBody Data data) {
        System.out.println(data.toString());
    }

    private ProjectDirectory toProjectDirectory(ProjectDirectoryApi projectDirectoryApi) {
        return new ProjectDirectory(projectDirectoryApi.getProjectName());
    }
}
