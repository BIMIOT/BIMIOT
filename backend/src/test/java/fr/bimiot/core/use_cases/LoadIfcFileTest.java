package fr.bimiot.core.use_cases;

import fr.bimiot.entrypoints.exception.type.BaseException;
import fr.bimiot.core.entities.Project;
import fr.bimiot.core.use_cases.providers.ProjectDatabaseProvider;
import fr.bimiot.fixtures.ProjectFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoadIfcFileTest {
    @InjectMocks
    LoadIfcFile loadIfcFile;

    @Mock
    ProjectDatabaseProvider projectDatabaseProvider;

    @Test
    void execute_shouldReturnCorrectBytesArray() throws IOException, BaseException {
        //  Given
        Project project = ProjectFixture.aCompleteProject();
        BDDMockito.doReturn(project.getIfc().getBytes()).when(projectDatabaseProvider).loadIFCFile(project.getName());
        //  When
        var result = loadIfcFile.execute(project.getName());
        //  Then
        assertNotNull(result);
        assertEquals(result, project.getIfc().getBytes());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    void execute_shouldThrowDomainException(String input) {
        assertThrows(BaseException.class, () -> loadIfcFile.execute(input));
    }
}