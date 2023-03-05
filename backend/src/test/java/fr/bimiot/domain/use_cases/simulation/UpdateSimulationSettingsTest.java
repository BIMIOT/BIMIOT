package fr.bimiot.domain.use_cases.simulation;

import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.providers.SimulatorProvider;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UpdateSimulationSettingsTest {
    @InjectMocks
    UpdateSimulationSettings updateSimulationSettings;
    @Mock
    SimulatorProvider simulatorProvider;

    @ParameterizedTest
    @CsvSource({
            "localhost, 8080",
            "localhost, 3000",
            "my.private.address, 4000"
    })
    void execute_shouldCallUpdateAddressOfProvider(String host, Integer port) throws DomainException {
        updateSimulationSettings.execute(host, port);
        verify(simulatorProvider, times(1)).updateAddress(host, port);
    }

    @ParameterizedTest
    @CsvSource({
            "localhost, 80",
            "localhost, 27020",
            " , 4000"
    })
    void execute_shouldThrowDomainException(String host, Integer port) throws DomainException {
        assertThrows(DomainException.class, () -> updateSimulationSettings.execute(host, port));
    }
}