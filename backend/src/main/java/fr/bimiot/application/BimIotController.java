package fr.bimiot.application;

import fr.bimiot.application.dto.RoomDTO;
import fr.bimiot.application.dto.RoomListDTO;
import fr.bimiot.domain.entities.Data;
import fr.bimiot.domain.entities.ProjectDirectory;
import fr.bimiot.domain.use_cases.CreateProject;
import fr.bimiot.domain.use_cases.GetAllProjects;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/bimiot")
public class BimIotController {
    private final static Base64.Decoder DECODER = Base64.getMimeDecoder();

    private final CreateProject createProjectUseCase;
    private final GetAllProjects getAllProjects;

    public BimIotController(CreateProject createProject, GetAllProjects getAllProjects) {
        this.createProjectUseCase = createProject;
        this.getAllProjects = getAllProjects;
    }

    @PostMapping("/project")
    public ProjectDirectoryApi createProject(@RequestBody ProjectDirectoryApi projectDirectoryApi) {
        return toProjectDirectoryApi(createProjectUseCase.execute(toProjectDirectory(projectDirectoryApi)));
    }

    @GetMapping("/projects")
    public List<String> getAllProjects() throws IOException {
        return getAllProjects.execute();
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

    @PostMapping("/mapping")
    public int createMapping(@RequestBody RoomDTO[] roomListDTO) {
        System.out.println(Arrays.toString(roomListDTO));
        return 0;
    }
}
