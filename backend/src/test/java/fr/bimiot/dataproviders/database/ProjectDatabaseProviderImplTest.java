package fr.bimiot.dataproviders.database;

import fr.bimiot.domain.entities.Project;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProjectDatabaseProviderImplTest {
    @InjectMocks
    ProjectDatabaseProviderImpl projectDatabaseProvider;

    @Mock
    ProjectJpaRepository projectJpaRepository;

    private static final MultipartFile IFC_FILE = new MockMultipartFile("ifc", "Hello".getBytes());

    private static final MultipartFile DATASET_FILE = new MockMultipartFile("dataset", "Hello".getBytes());

    @Test
    void create_shouldInsertByUsingJpaRepository() throws IOException {
        //  Given
        Project project = new Project(null, "project 1", IFC_FILE, DATASET_FILE);
        ArgumentCaptor<ProjectJpa> projectJpaArgumentCaptor = ArgumentCaptor.forClass(ProjectJpa.class);
        ProjectJpa projectJpaInput = new ProjectJpa();
        projectJpaInput.setName(project.getName());
        projectJpaInput.setIfc(toBinary(IFC_FILE));
        projectJpaInput.setDataset(toBinary(DATASET_FILE));

        ProjectJpa projectJpaOutput = new ProjectJpa();
        projectJpaOutput.setId("projectId");
        projectJpaOutput.setName(project.getName());

        BDDMockito.doReturn(projectJpaOutput).when(projectJpaRepository).insert(projectJpaInput);

        //  When
        String projectId = projectDatabaseProvider.create(project);

        //  Then
        verify(projectJpaRepository).insert(projectJpaArgumentCaptor.capture());
        assertEquals(project.getName(), projectJpaArgumentCaptor.getValue().getName());
        assertEquals("projectId", projectId);
    }

    private Binary toBinary(MultipartFile file) throws IOException {
        return new Binary(BsonBinarySubType.BINARY, file.getBytes());
    }
}