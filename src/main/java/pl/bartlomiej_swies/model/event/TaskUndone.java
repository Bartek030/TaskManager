package pl.bartlomiej_swies.model.event;

import pl.bartlomiej_swies.model.Task;

import java.time.Clock;

public class TaskUndone extends TaskEvent {
    TaskUndone(final Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
