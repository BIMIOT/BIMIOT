package fr.bimiot.application;

import fr.bimiot.domain.entities.ProjectDirectory;
import fr.bimiot.domain.use_cases.CreateProject;
import fr.bimiot.domain.use_cases.StartSimulation;
import fr.bimiot.domain.use_cases.StopSimulation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/bimiot")
public class BimIotAdapter {
    private final StartSimulation startSimulationUseCase;
    private final StopSimulation stopSimulationUseCase;

    private final CreateProject createProjectUseCase;

    public BimIotAdapter(StartSimulation startSimulation,
                         StopSimulation stopSimulation,
                         CreateProject createProject){
        this.startSimulationUseCase = startSimulation;
        this.stopSimulationUseCase = stopSimulation;
        this.createProjectUseCase = createProject;
    }

    @PostMapping("/project")
    public ProjectDirectoryApi createProject(@RequestBody ProjectDirectoryApi projectDirectoryApi){
        return toProjectDirectoryApi(createProjectUseCase.execute(toProjectDirectory(projectDirectoryApi)));
    }

    @PostMapping(value = "/fileUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFile(@RequestParam("file")MultipartFile file){
        return String.format("File %s uploaded successfully !", file.getOriginalFilename());
    }

    private ProjectDirectory toProjectDirectory(ProjectDirectoryApi projectDirectoryApi){
        return new ProjectDirectory(projectDirectoryApi.getDirectoryName());
    }

    private ProjectDirectoryApi toProjectDirectoryApi(ProjectDirectory projectDirectory){
        var directory = new ProjectDirectoryApi();
        directory.setDirectoryName(projectDirectory.name());
        return directory;
    }
}
