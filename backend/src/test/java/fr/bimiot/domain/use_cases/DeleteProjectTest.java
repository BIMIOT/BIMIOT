package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.entities.Project;
import fr.bimiot.domain.exception.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeleteProjectTest {
    @InjectMocks
    DeleteProject deleteProject;
    @InjectMocks
    CreateProject createProject;

    @InjectMocks
    GetFile getFile;

    @Mock
    ProjectPort projectPort;
    private final static Path PROJECTS_FOLDER = Paths.get("Projects");

    private final static Path TEST_PROJECT = PROJECTS_FOLDER.resolve("deleteTest");




    @Test
    public void deleteProject_byGivenProjectName() throws DomainException {
        //Given
        Project project = new Project("deleteTest");
        createProject.createFolder(project);
        assertTrue(Files.isDirectory(TEST_PROJECT));

        //When
        deleteProject.execute(project.name());

        //Then
        assertFalse(Files.isDirectory(TEST_PROJECT));
        assertThrows(AssertionError.class,() ->{
            getFile.execute("deleteTest");
        });
    }

}
