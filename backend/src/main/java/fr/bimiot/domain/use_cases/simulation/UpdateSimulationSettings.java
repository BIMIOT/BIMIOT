package fr.bimiot.domain.use_cases.simulation;

import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.providers.SimulatorProvider;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UpdateSimulationSettings {
    private final SimulatorProvider simulatorProvider;

    private static final String IS_NOT_VALID_TEXT = " is not valid";

    public UpdateSimulationSettings(SimulatorProvider simulatorProvider) {
        this.simulatorProvider = simulatorProvider;
    }

    public void execute(String host, Integer port) throws DomainException {
        if (isNotValidHost(host)) {
            throw new DomainException("Host : " + host + IS_NOT_VALID_TEXT);
        }
        if (isNotValidPort(port)) {
            throw new DomainException("Port : " + port + IS_NOT_VALID_TEXT);
        }
        if (isNotValidCompleteAddress(host, port)) {
            throw new DomainException("Address : " + host + ":" + port + IS_NOT_VALID_TEXT + " because is aldready used");
        }
        simulatorProvider.updateAddress(host, port);
    }

    private boolean isNotValidHost(String host) {
        return host == null || host.isEmpty() || host.isBlank();
    }

    private boolean isNotValidPort(Integer port) {
        return port == null || port < 0;
    }

    private boolean isNotValidCompleteAddress(String host, Integer port) {
        return (Objects.equals(host, "localhost") && Objects.equals(port, 80))
                || (Objects.equals(host, "localhost") && Objects.equals(port, 27020));
    }
}
