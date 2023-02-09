package fr.bimiot.domain.use_cases;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetAllProjectsTest {
    @InjectMocks
    GetAllProjects getAllProjects;

    private final static String PARENT_TEST_FOLDER = "Projects";
    private final static List<String> TEST_FOLDERS = List.of("Folder1","Folder2","Folder3");

    @BeforeAll
    public void init() throws IOException {
        Files.createDirectory(Paths.get(PARENT_TEST_FOLDER));
        Files.createDirectory(Paths.get(PARENT_TEST_FOLDER+"/Folder1"));
        Files.createDirectory(Paths.get(PARENT_TEST_FOLDER+"/Folder2"));
        Files.createDirectory(Paths.get(PARENT_TEST_FOLDER+"/Folder3"));
    }

    @AfterAll
    public void afterAll() throws IOException {
        Files.walkFileTree(Paths.get(PARENT_TEST_FOLDER), new SimpleFileVisitor<>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.deleteIfExists(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.deleteIfExists(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Test
    public void givenSeveralFolder_whenGetAllProjects_thenReturnAllProjectNames() throws IOException {
        List<String> results = getAllProjects.execute();
        assertNotNull(results);
        assertTrue(List.of("Folder1","Folder2","Folder3").containsAll(results));
        assertEquals(TEST_FOLDERS.size(), results.size());
    }
}