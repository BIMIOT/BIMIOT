package fr.bimiot.application;

import fr.bimiot.application.dto.RoomDTO;
import fr.bimiot.domain.entities.Data;
import fr.bimiot.domain.use_cases.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/bimiot")
public class BimIotController {
    private final CreateProject createProjectUseCase;
    private final GetAllProjects getAllProjects;
    private final ManageData manageData;
    private final ManageSimulation manageSimulation;
    private final GetFile getFile;

    @Autowired
    public BimIotController(CreateProject createProject, GetAllProjects getAllProjects, GetFile getFile,ManageData manageData, ManageSimulation manageSimulation) {
        this.createProjectUseCase = createProject;
        this.getAllProjects = getAllProjects;
        this.manageData = manageData;
        this.manageSimulation = manageSimulation;
        this.getFile = getFile;
    }

    @GetMapping("/projects")
    public ResponseEntity<List<String>> getAllProjects() {
        return ResponseEntity.status(HttpStatus.OK).body(getAllProjects.execute());
    }

    @PutMapping(value = "/sendData", consumes = "application/json")
    public void sendData(@RequestBody Data data) {
        System.out.println(data.toString());
        manageData.execute(data);
    }

    @PutMapping(value="/start/{simulation_name}")
    public int start(@PathVariable String simulation_name) {
        manageSimulation.executeStart(simulation_name);
        return 0;
    }

    @PutMapping(value="/stop/{simulation_name}")
    public int stop(@PathVariable String simulation_name) {
        manageSimulation.executeStop(simulation_name);
        return 0;
    }

    @PostMapping("/mapping")
    public int createMapping(@RequestBody RoomDTO[] roomListDTO) {
        System.out.println(Arrays.toString(roomListDTO));
        manageData.setRoomListDTO(roomListDTO);
        return 0;
    }
}
