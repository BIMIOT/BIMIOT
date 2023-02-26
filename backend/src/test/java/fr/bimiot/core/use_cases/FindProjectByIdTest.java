package fr.bimiot.core.use_cases;

import fr.bimiot.core.exception.DomainException;
import fr.bimiot.core.use_cases.providers.ProjectProvider;
import fr.bimiot.fixtures.ProjectFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FindProjectByIdTest {

    @InjectMocks
    FindProjectById findProjectById;
    @Mock
    ProjectProvider projectProvider;

    @Test
    void execute_shouldReturnProjectWithFromProvider() throws DomainException {
        //  Given
        var project = ProjectFixture.aCompleteProject();
        BDDMockito.doReturn(Optional.of(project)).when(projectProvider).findById(project.getId());
        //  When
        var result = findProjectById.execute(project.getId());
        //  Then
        assertThat(result).isEqualTo(project);
    }

    @Test
    void execute_shoudThrowDomainException() {
        //  Given
        BDDMockito.doReturn(Optional.empty()).when(projectProvider).findById(ProjectFixture.aCompleteProject().getId());
        //  When / Then
        assertThrows(DomainException.class, () -> findProjectById.execute(ProjectFixture.aCompleteProject().getId()));
    }
}