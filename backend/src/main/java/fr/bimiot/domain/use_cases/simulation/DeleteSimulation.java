package fr.bimiot.domain.use_cases.simulation;

import fr.bimiot.domain.use_cases.providers.SimulatorProvider;
import org.springframework.stereotype.Component;

@Component
public class DeleteSimulation {
    private final SimulatorProvider simulatorProvider;

    public DeleteSimulation(SimulatorProvider simulatorProvider) {
        this.simulatorProvider = simulatorProvider;
    }

    /**
     * Delete corresponding simulation in simulator
     *
     * @param projectName's project
     */
    public void execute(String projectName) {
        simulatorProvider.deleteSimulation(projectName);
    }
}
