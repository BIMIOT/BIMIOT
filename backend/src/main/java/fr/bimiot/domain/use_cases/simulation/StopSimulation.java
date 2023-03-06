package fr.bimiot.domain.use_cases.simulation;

import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.providers.SimulatorProvider;
import fr.bimiot.utils.Utils;
import org.springframework.stereotype.Component;

@Component
public class StopSimulation {
    private final SimulatorProvider simulatorProvider;

    public StopSimulation(SimulatorProvider simulatorProvider) {
        this.simulatorProvider = simulatorProvider;
    }

    /**
     * Stop corresponding simulation in simulator
     * @param projectName's project
     */
    public void execute(String projectName) throws DomainException {
        if(Utils.isNotValidProjectName(projectName)){
            throw new DomainException("Project name is not valid");
        }
        simulatorProvider.stop(projectName);
    }
}
