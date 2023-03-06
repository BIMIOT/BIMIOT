package fr.bimiot.domain.use_cases.simulation;

import fr.bimiot.domain.use_cases.providers.SimulatorProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class CreateSimulation {
    private final SimulatorProvider simulatorProvider;

    public CreateSimulation(SimulatorProvider simulatorProvider) {
        this.simulatorProvider = simulatorProvider;
    }

    public void execute(String projectName, MultipartFile dataset) {
        simulatorProvider.createSimulation(projectName, dataset);
    }
}
