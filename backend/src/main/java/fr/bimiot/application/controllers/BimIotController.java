package fr.bimiot.application.controllers;

import fr.bimiot.domain.entities.*;

import fr.bimiot.domain.entities.Room;
import fr.bimiot.domain.use_cases.projects.CreateProject;
import fr.bimiot.domain.use_cases.projects.GetAllProjects;
import fr.bimiot.domain.use_cases.ManageData;
import fr.bimiot.domain.use_cases.ManageSimulation;
import fr.bimiot.domain.use_cases.GetFile;
import fr.bimiot.simulator.ConverterEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public BimIotController(CreateProject createProject, GetAllProjects getAllProjects, GetFile getFile,ManageData manageData, ManageSimulation manageSimulation) {
        this.createProjectUseCase = createProject;
        this.getAllProjects = getAllProjects;
        this.manageData = manageData;
        this.manageSimulation = manageSimulation;
        this.getFile = getFile;
    }

    @Deprecated
    @GetMapping("/projects")
    public ResponseEntity<List<String>> getAllProjects() {
        return ResponseEntity.status(HttpStatus.OK).body(getAllProjects.execute());
    }

    @PutMapping(value = "/sendData", consumes = "application/json")
    public void sendData(@RequestBody Data data) {
        var event = new ConverterEvent(this, manageData.execute(data));
        System.out.println("before publish : " + event.getMessage());
        applicationEventPublisher.publishEvent(event);
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
    public int createMapping(@RequestBody List<Room> roomListDTO) {
        System.out.println(roomListDTO);
        manageData.setRoomListDTO(roomListDTO);
        return 0;
    }

    @PostMapping("/colors")
    public int createColors(@RequestBody TypesColors typesColors) {
        System.out.println(typesColors);
        manageData.setTypesColors(typesColors);
        return 0;
    }
}
