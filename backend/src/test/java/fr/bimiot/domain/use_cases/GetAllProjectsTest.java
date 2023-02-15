/*
package fr.bimiot.domain.use_cases;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetAllProjectsTest {
    @InjectMocks
    GetAllProjects getAllProjects;

    private final static Path PROJECTS_FOLDER = Paths.get("Projects");
    private final static Path TEST_FOLDER = PROJECTS_FOLDER.resolve("Folder1");

    @Test
    public void givenSeveralFolder_whenGetAllProjects_thenReturnAllProjectNames() throws IOException {
        if(!Files.isDirectory(PROJECTS_FOLDER)){
            Files.createDirectory(PROJECTS_FOLDER);
        }
        Files.createDirectory(TEST_FOLDER);
        List<String> results = getAllProjects.execute();
        assertNotNull(results);
        assertTrue(List.of("Folder1").containsAll(results));
        Files.delete(TEST_FOLDER);
    }
}*/
