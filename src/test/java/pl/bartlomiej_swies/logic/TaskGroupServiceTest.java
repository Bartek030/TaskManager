package pl.bartlomiej_swies.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.bartlomiej_swies.model.TaskGroup;
import pl.bartlomiej_swies.model.TaskGroupRepository;
import pl.bartlomiej_swies.model.TaskRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    private TaskRepository mockTaskRepository;

    @BeforeEach
    void initializeMockTaskRepository() {
        mockTaskRepository = mock(TaskRepository.class);
    }

    @Test
    void toggleGroup_should_throw_IllegalStateException_when_exists_by_done_is_false_and_group_id() {

        // given
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(true);
        // system under test
        var toTest = new TaskGroupService(null, mockTaskRepository);

        // when
        // then
        assertThrows(IllegalStateException.class, () -> toTest.toggleGroup(1));
    }

    @Test
    void toggleGroup_should_throw_IllegalArgumentException_when_group_not_found_by_id() {

        // given
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(false);
        // and
        TaskGroupRepository mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.empty());

        // system under test
        TaskGroupService toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> toTest.toggleGroup(1));
    }

    @Test
    void toggleGroup_should_change_done_status_and_save() {

        // given
        TaskGroup taskGroup = new TaskGroup();
        // and
        var beforeToggle = taskGroup.isDone();
        // and
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(false);
        // and
        TaskGroupRepository mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.of(taskGroup));
        // system under test
        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);

        // when
        toTest.toggleGroup(1);

        // then
        assertThat(taskGroup.isDone()).isEqualTo(!beforeToggle);
    }
}