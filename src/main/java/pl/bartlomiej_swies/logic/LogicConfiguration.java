package pl.bartlomiej_swies.logic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.bartlomiej_swies.TaskConfigurationProperties;
import pl.bartlomiej_swies.model.ProjectRepository;
import pl.bartlomiej_swies.model.TaskGroupRepository;
import pl.bartlomiej_swies.model.TaskRepository;

@Configuration
public class LogicConfiguration {
    @Bean
    ProjectService projectService (final ProjectRepository repository, final TaskGroupRepository taskGroupRepository, final TaskConfigurationProperties config) {
        return new ProjectService(repository, taskGroupRepository, config);
    }

    @Bean
    TaskGroupService taskGroupService (final TaskGroupRepository repository, final TaskRepository taskRepository) {
        return new TaskGroupService(repository, taskRepository);
    }
}
