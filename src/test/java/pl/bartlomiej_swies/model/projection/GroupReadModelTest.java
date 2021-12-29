package pl.bartlomiej_swies.model.projection;

import org.junit.jupiter.api.Test;
import pl.bartlomiej_swies.model.Task;
import pl.bartlomiej_swies.model.TaskGroup;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class GroupReadModelTest {

    @Test
    void constructor_without_deadlines_should_creates_null_deadline(){
        // given
        var source = new TaskGroup();
        source.setDescription("foo");
        source.setTasks(Set.of(new Task("bar", null)));

        // when
        var result = new GroupReadModel(source);

        //then
        assertThat(result).hasFieldOrPropertyWithValue("deadline", null);
    }
}