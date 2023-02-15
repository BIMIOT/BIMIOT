package fr.bimiot.domain.use_cases.projects;

import fr.bimiot.domain.exception.DomainException;
import fr.bimiot.domain.use_cases.DeleteProject;
import fr.bimiot.domain.use_cases.providers.ProjectDatabaseProvider;
import fr.bimiot.domain.use_cases.providers.ProjectFileProvider;
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
public class DeleteProjectTest {
    @InjectMocks
    DeleteProject deleteProject;

    @Mock
    ProjectDatabaseProvider projectDatabaseProvider;

    @Mock
    ProjectFileProvider projectFileProvider;

    private final static String PROJECT_NAME = "deleteTest";

    @Test
    public void deleteProject_byGivenProjectName() throws DomainException {
        //Given

        //When
        deleteProject.execute(PROJECT_NAME);

        //Then
        verify(projectFileProvider, times(1)).delete(PROJECT_NAME);
    }

}
