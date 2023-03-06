package fr.bimiot.domain.use_cases.simulation;

import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.providers.SimulatorProvider;
import org.springframework.stereotype.Component;

@Component
public class PauseSimulation {
    private final SimulatorProvider simulatorProvider;

    public PauseSimulation(SimulatorProvider simulatorProvider) {
        this.simulatorProvider = simulatorProvider;
    }

    /**
     * Pause corresponding simulation in simulator
     *
     * @param projectName's project
     */
    public void execute(String projectName) throws DomainException {
        if (isNotValidProjectName(projectName)) {
            throw new DomainException("Project name is not valid");
        }
        simulatorProvider.pause(projectName);
    }

    private boolean isNotValidProjectName(String projectName) {
        return projectName == null || projectName.isBlank() || projectName.isEmpty();
    }
}
