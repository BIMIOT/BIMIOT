package fr.bimiot.domain.use_cases.simulation;

import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.providers.SimulatorProvider;
import fr.bimiot.fixtures.ProjectFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PauseSimulationTest {
    @InjectMocks
    PauseSimulation pauseSimulation;
    @Mock
    SimulatorProvider simulatorProvider;

    @Test
    void execute_shouldCallPauseOfProvider() throws DomainException {
        var projectName = ProjectFixture.aCompleteProject().getName();
        pauseSimulation.execute(projectName);
        verify(simulatorProvider, times(1)).pause(projectName);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    void execute_shouldThrowDomainException(String input) {
        Exception exception = assertThrows(DomainException.class, () -> pauseSimulation.execute(input));
        assertNotNull(exception);
        assertEquals("Project name is not valid", exception.getMessage());
    }
}