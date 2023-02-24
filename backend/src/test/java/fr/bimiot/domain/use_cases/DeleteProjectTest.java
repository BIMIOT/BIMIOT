package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.providers.ProjectDatabaseProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteProjectTest {
    @InjectMocks
    DeleteProject deleteProject;
    @Mock
    ProjectDatabaseProvider projectDatabaseProvider;
    private final static String PROJECT_NAME = "deleteTest";

    @Test
    void deleteProject_byGivenProjectName() throws DomainException {
        //When
        deleteProject.execute(PROJECT_NAME);

        //Then
        verify(projectDatabaseProvider, times(1)).delete(PROJECT_NAME);
    }

}
