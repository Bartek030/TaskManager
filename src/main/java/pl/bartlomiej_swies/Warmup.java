package pl.bartlomiej_swies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import pl.bartlomiej_swies.model.Task;
import pl.bartlomiej_swies.model.TaskGroup;
import pl.bartlomiej_swies.model.TaskGroupRepository;

import java.util.Set;

@Component
class Warmup implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(Warmup.class);
    private final TaskGroupRepository groupRepository;

    Warmup(final TaskGroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        logger.info("Application warmup after content refreshed");
        final String description = "ApplicationContextEvent";
        if(groupRepository.existsByDescription(description)) {
            logger.info("No required group found! Adding it!");
            var group = new TaskGroup();
            group.setDescription(description);
            group.setTasks(Set.of(
                new Task("ContextClosedEvent", null, group),
                new Task("ContextRefreshedEvent", null, group),
                new Task("ContextStoppedEvent", null, group),
                new Task("ContextStartedEvent", null, group)
            ));
            groupRepository.save(group);
        }
    }
}
