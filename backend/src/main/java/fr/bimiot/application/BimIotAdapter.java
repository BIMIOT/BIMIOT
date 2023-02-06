package fr.bimiot.application;

import fr.bimiot.domain.use_cases.StartSimulation;
import fr.bimiot.domain.use_cases.StopSimulation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BimIotAdapter {
    private final StartSimulation startSimulation;
    private final StopSimulation stopSimulation;

    public BimIotAdapter(StartSimulation startSimulation, StopSimulation stopSimulation){
        this.startSimulation = startSimulation;
        this.stopSimulation = stopSimulation;
    }

    @GetMapping
    public String getString(){
        return null;
    }
}
