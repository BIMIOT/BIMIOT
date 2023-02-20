package fr.bimiot.domain.use_cases;

import fr.bimiot.dataproviders.exception.DataBaseException;
import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.use_cases.providers.ProjectDatabaseProvider;
import fr.bimiot.fixtures.ProjectFixture;
import fr.bimiot.fixtures.TypesColorsFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class UpdateSensorsColorsTest {
    @InjectMocks
    UpdateSensorsColors updateSensorsColors;

    @Mock
    ProjectDatabaseProvider projectDatabaseProvider;

    @Test
    void execute_shouldReturnSameProjectWithColorsSensors() throws DataBaseException {
        //  Given
        Project project = ProjectFixture.aProjectWithoutSensorsAndWithoutId();
        BDDMockito.doReturn(ProjectFixture.aProjectWithOnlyTemperatureSensor())
                .when(projectDatabaseProvider)
                .updateSensorsColorsByProjectName(project.getName(), TypesColorsFixture.allSensors());
        //  When
        Project result = updateSensorsColors.execute(project.getName(), TypesColorsFixture.allSensors());

        //  Then
        assertNotNull(result);
        assertEquals(project, result);
    }

}