package fr.bimiot.domain.use_cases;

import fr.bimiot.application.exception.type.BaseException;
import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.use_cases.providers.ProjectProvider;
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
    ProjectProvider projectProvider;

    @Test
    void execute_shouldReturnCorrectBytesArray() throws IOException, BaseException {
        //  Given
        Project project = ProjectFixture.aCompleteProject();
        BDDMockito.doReturn(project.getIfc().getBytes()).when(projectProvider).loadIFCFile(project.getName());
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