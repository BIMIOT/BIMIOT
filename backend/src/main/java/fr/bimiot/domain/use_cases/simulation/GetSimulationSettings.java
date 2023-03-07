package fr.bimiot.domain.use_cases.simulation;

import fr.bimiot.domain.use_cases.providers.SimulatorProvider;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class GetSimulationSettings {
    private final SimulatorProvider simulatorProvider;

    GetSimulationSettings(SimulatorProvider simulatorProvider) {
        this.simulatorProvider = simulatorProvider;

    }

    public String execute() {
        return simulatorProvider.getCompleteSimulatorAddress();
    }

}
