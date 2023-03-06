package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.providers.ProjectProvider;
import fr.bimiot.domain.use_cases.simulation.DeleteSimulation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeleteProjectTest {
    @InjectMocks
    DeleteProject deleteProject;

    @Mock
    ProjectProvider projectProvider;
    @Mock
    DeleteSimulation deleteSimulation;


    private final static String PROJECT_NAME = "deleteTest";

    @Test
    void deleteProject_byGivenProjectName() throws DomainException {
        //When
        deleteProject.execute(PROJECT_NAME);

        //Then
        verify(projectProvider, times(1)).delete(PROJECT_NAME);
        verify(deleteSimulation, times(1)).execute(PROJECT_NAME);
    }

}
