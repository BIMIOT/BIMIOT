package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.entities.SensorType;
import fr.bimiot.domain.use_cases.providers.ProjectDatabaseProvider;
import fr.bimiot.fixtures.SensorColorMapFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class GetSensorColorMapTest {

    @InjectMocks
    GetSensorColorMap getSensorColorMap;

    @Mock
    ProjectDatabaseProvider projectDatabaseProvider;

    @Test
    void execute_shouldReturnCompleteSensorColorMap() {
        //  Given
        String projectName = "Project X";
        BDDMockito.doReturn(SensorColorMapFixture.sensorTypeListMapDomain())
                .when(projectDatabaseProvider)
                .findSensorColorMapByProjectName(projectName);
        //  When
        var result = getSensorColorMap.execute(projectName);
        //  Then
        assertNotNull(result);
        assertTrue(result.keySet().containsAll(Arrays.stream(SensorType.values()).filter(type -> !SensorType.END.equals(type)).toList()));
    }
}
