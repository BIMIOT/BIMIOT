package fr.bimiot.application;

import fr.bimiot.domain.entities.Data;
import fr.bimiot.domain.entities.ProjectDirectory;
import fr.bimiot.domain.use_cases.CreateProject;
import fr.bimiot.domain.use_cases.StartSimulation;
import fr.bimiot.domain.use_cases.StopSimulation;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@RequestMapping("/api/bimiot")
public class BimIotAdapter {
    private final static Base64.Decoder DECODER = Base64.getMimeDecoder();
    private final StartSimulation startSimulationUseCase;
    private final StopSimulation stopSimulationUseCase;

    private final CreateProject createProjectUseCase;

    public BimIotAdapter(StartSimulation startSimulation,
                         StopSimulation stopSimulation,
                         CreateProject createProject) {
        this.startSimulationUseCase = startSimulation;
        this.stopSimulationUseCase = stopSimulation;
        this.createProjectUseCase = createProject;
    }

    @PostMapping("/project")
    public ProjectDirectoryApi createProject(@RequestBody ProjectDirectoryApi projectDirectoryApi) {
        return toProjectDirectoryApi(createProjectUseCase.execute(toProjectDirectory(projectDirectoryApi)));
    }

    @PutMapping(value="/sendData", consumes = "application/json")
    public void sendData(@RequestBody Data data) {
        System.out.println(data.toString());
    }

    private ProjectDirectory toProjectDirectory(ProjectDirectoryApi projectDirectoryApi) {
        return new ProjectDirectory(projectDirectoryApi.getDirectoryName(),
                new String(DECODER.decode(getContentOfFile(projectDirectoryApi.getIfcFile()))),
                new String(DECODER.decode(getContentOfFile(projectDirectoryApi.getDatasetFile())))
        );
    }

    private ProjectDirectoryApi toProjectDirectoryApi(ProjectDirectory projectDirectory) {
        var directory = new ProjectDirectoryApi();
        directory.setDirectoryName(projectDirectory.name());
        return directory;
    }

    private String getContentOfFile(String encodedFile) {
        return encodedFile.split(",")[1];
    }
}
