package pl.bartlomiej_swies.model.event;

import pl.bartlomiej_swies.model.Task;

import java.time.Clock;

public class TaskDone extends TaskEvent {
    TaskDone(final Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
