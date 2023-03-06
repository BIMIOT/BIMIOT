package fr.bimiot.domain.use_cases.simulation;

import fr.bimiot.domain.use_cases.providers.SimulatorProvider;
import org.springframework.stereotype.Component;

@Component
public class GetCompleteSimulatorAddress {
    private final SimulatorProvider simulatorProvider;

    public GetCompleteSimulatorAddress(SimulatorProvider simulatorProvider) {
        this.simulatorProvider = simulatorProvider;
    }

    public String execute(){
        return simulatorProvider.getCompleteSimulatorAddress();
    }
}
