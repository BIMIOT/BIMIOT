package fr.bimiot.application;

import fr.bimiot.domain.use_cases.CreateProject;
import fr.bimiot.domain.use_cases.StartSimulation;
import fr.bimiot.domain.use_cases.StopSimulation;
import org.springframework.web.bind.annotation.*;

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
    public String createProject(String name){
        return createProjectUseCase.execute(name);
    }
}
