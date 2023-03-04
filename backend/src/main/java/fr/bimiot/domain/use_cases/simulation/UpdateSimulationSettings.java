package fr.bimiot.domain.use_cases.simulation;

import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.providers.SimulatorProvider;
import org.springframework.stereotype.Component;

@Component
public class UpdateSimulationSettings {
    private final SimulatorProvider simulatorProvider;

    public UpdateSimulationSettings(SimulatorProvider simulatorProvider) {
        this.simulatorProvider = simulatorProvider;
    }

    public void execute(String host, Integer port) throws DomainException {
        if (isNotValidHost(host)) {
            throw new DomainException("Host address is not valid");
        }
        if (isNotValidPort(port)) {
            throw new DomainException("Port address is not valid");
        }
        simulatorProvider.updateAddress(host, port);
    }

    private boolean isNotValidHost(String host) {
        return host == null || host.isEmpty() || host.isBlank();
    }

    private boolean isNotValidPort(Integer port) {
        return port == null || port < 0 || port == 80 || port == 27020;
    }
}
