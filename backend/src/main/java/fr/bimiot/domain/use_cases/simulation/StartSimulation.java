package fr.bimiot.domain.use_cases.simulation;

import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.providers.SimulatorProvider;
import fr.bimiot.utils.Utils;
import org.springframework.stereotype.Component;

@Component
public class StartSimulation {
    private final SimulatorProvider simulatorProvider;

    public StartSimulation(SimulatorProvider simulatorProvider) {
        this.simulatorProvider = simulatorProvider;
    }

    /**
     * Start corresponding simulation in simulator
     *
     * @param projectName's project
     */
    public void execute(String projectName) throws DomainException {
        if(Utils.isNotValidProjectName(projectName)){
            throw new DomainException("Project name is not valid");
        }
        simulatorProvider.start(projectName);
    }
}
