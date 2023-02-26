package fr.bimiot.core.use_cases;

import fr.bimiot.core.use_cases.providers.ProjectProvider;
import fr.bimiot.fixtures.ProjectFixture;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class FindAllProjectsTest {

    @InjectMocks
    FindAllProjects findAllProjects;
    @Mock
    ProjectProvider projectProvider;

    @Test
    void execute_shouldReturnProjectsFromProvider() {
        //  Given
        var projects = Lists.newArrayList(ProjectFixture.aCompleteProject());
        BDDMockito.doReturn(projects).when(projectProvider).findAll();
        //  When
        var results = findAllProjects.execute();
        //  Then
        assertThat(results).containsExactlyElementsOf(projects);
    }
}