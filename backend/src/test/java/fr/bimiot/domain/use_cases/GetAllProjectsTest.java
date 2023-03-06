package fr.bimiot.domain.use_cases;

import fr.bimiot.domain.use_cases.providers.ProjectProvider;
import fr.bimiot.fixtures.ProjectFixture;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GetAllProjectsTest {
    @InjectMocks
    GetAllProjects getAllProjects;
    @Mock
    ProjectProvider projectProvider;

    @Test
    void execute_shouldReturnProjectsFromProvider() {
        var project = ProjectFixture.aCompleteProject();
        BDDMockito.doReturn(Lists.newArrayList(project)).when(projectProvider).getAllProjects();
        var results = getAllProjects.execute();
        assertEquals(1, results.size());
        var result = results.get(0);
        assertEquals(project, result);
    }

    @Test
    void execute_shouldReturnEmptyListFromProvider(){
        BDDMockito.doReturn(Lists.newArrayList()).when(projectProvider).getAllProjects();
        var results = getAllProjects.execute();
        assertTrue(results.isEmpty());
    }
}