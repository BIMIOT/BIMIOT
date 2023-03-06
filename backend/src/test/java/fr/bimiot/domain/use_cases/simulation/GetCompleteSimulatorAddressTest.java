package fr.bimiot.domain.use_cases.simulation;

import fr.bimiot.domain.use_cases.providers.SimulatorProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GetCompleteSimulatorAddressTest {
    @InjectMocks
    GetCompleteSimulatorAddress getCompleteSimulatorAddress;
    @Mock
    SimulatorProvider simulatorProvider;

    @Test
    void execute_shouldReturnCompleteSimulatorAddressFromProvider() {
        var completeAddress = "http://host.docker.internal:8090";
        BDDMockito.doReturn(completeAddress)
                .when(simulatorProvider)
                .getCompleteSimulatorAddress();
        var result = getCompleteSimulatorAddress.execute();
        assertNotNull(result);
        assertEquals(completeAddress, result);
    }
}