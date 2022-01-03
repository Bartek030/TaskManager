package pl.bartlomiej_swies.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.bartlomiej_swies.TaskConfigurationProperties;
import pl.bartlomiej_swies.model.*;
import pl.bartlomiej_swies.model.projection.GroupReadModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("Should throw IllegalStateException when configured to allow just 1 group and the other undone group exists")
    void createGroup_no_multiple_groups_config_and_undone_groups_exists_throws_IllegalStateException() {

        // given
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(true);
        // and
        TaskConfigurationProperties mockConfig = configurationReturning(false);
        // system under test
        var toTest = new ProjectService(null, mockGroupRepository, null, mockConfig);

        // when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 500));
        // then
        assertThat(exception).isInstanceOf(IllegalStateException.class)
                            .hasMessageContaining("one undone group");
    }

    @Test
    @DisplayName("Should throw IllegalSArgumentException when configuration ok and no projects for given id")
    void createGroup_configuration_is_ok_and_no_projects_throws_IllegalArgumentException() {

        // given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        // and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        // system under test
        var toTest = new ProjectService(mockRepository, null, null, mockConfig);

        // when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 500));
        // then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("Should throw IllegalSArgumentException when configured to allow just 1 group and no groups and projects for given id")
    void createGroup_no_multiple_groups_config_and_no_undone_groups_exists_and_no_projects_throws_IllegalArgumentException() {

        // given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        // and
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(false);
        // and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        // system under test
        var toTest = new ProjectService(mockRepository, mockGroupRepository, null, mockConfig);

        // when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 500));
        // then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("Should create a new group from project")
    void createGroup_configuration_is_ok_existing_project_creates_and_saves_group() {

        //given
        var today = LocalDate.now().atStartOfDay();
        // and
        var project = projectWith("bar", Set.of(-1, -2));
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.of(project));
        //and
        InMemoryGroupRepository inMemoryGroupRepository = inMemoryGroupRepository();
        var serviceWithInMemoryRepository = dummyGroupService(inMemoryGroupRepository);
        int countBeforeCall = inMemoryGroupRepository.count();
        // and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        // system under test
        var toTest = new ProjectService(mockRepository, inMemoryGroupRepository, serviceWithInMemoryRepository, mockConfig);

        // when
        GroupReadModel result = toTest.createGroup(today, 1);

        //then
        assertThat(result.getDescription()).isEqualTo("bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("foo"));
        assertThat(countBeforeCall + 1).isEqualTo(inMemoryGroupRepository.count());
    }

    private TaskGroupService dummyGroupService(InMemoryGroupRepository inMemoryGroupRepository) {
        return new TaskGroupService(inMemoryGroupRepository, null);
    }

    private Project projectWith(final String projectDescription, Set<Integer> daysToDeadline) {
        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        Set<ProjectSteps> steps = daysToDeadline.stream()
                                                .map(days -> {
                                                    var step = mock(ProjectSteps.class);
                                                    when(step.getDescription()).thenReturn("foo");
                                                    when(step.getDaysToDeadline()).thenReturn(days);
                                                    return step;
                                                }).collect(Collectors.toSet());
        when(result.getSteps()).thenReturn(steps);
        return result;
    }

    private TaskGroupRepository groupRepositoryReturning(final boolean result) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(result);
        return mockGroupRepository;
    }

    private TaskConfigurationProperties configurationReturning(final boolean result) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(result);
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        return mockConfig;
    }

    private InMemoryGroupRepository inMemoryGroupRepository() {
        return new InMemoryGroupRepository() ;
    }

    private static class InMemoryGroupRepository implements TaskGroupRepository {
            private int index = 0;
            private Map<Integer, TaskGroup> map = new HashMap<>();

            public int count() {
                return map.values().size();
            }

            @Override
            public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
        }

            @Override
            public Optional<TaskGroup> findById(final Integer id) {
            return Optional.ofNullable(map.get(id));
        }

            @Override
            public TaskGroup save(final TaskGroup entity) {
            if(entity.getId() == 0) {
                try {
                    var field = TaskGroup.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, ++index);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            map.put(entity.getId(), entity);
            return entity;
        }

            @Override
            public boolean existsByDoneIsFalseAndProject_Id(final Integer projectId) {
            return map.values().stream()
                    .filter(group -> !group.isDone())
                    .anyMatch(group -> group.getProject() != null && group.getProject().getId() == projectId);
        }

        @Override
        public boolean existsByDescription(final String description) {
            return map.values().stream().anyMatch(group -> group.getDescription().equals(description));
        }
    }
}