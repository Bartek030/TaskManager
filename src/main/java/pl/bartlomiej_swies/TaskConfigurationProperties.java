package pl.bartlomiej_swies;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("task")
public class TaskConfigurationProperties {
    private boolean allowMultipleFromTemplate;

    public boolean isAllowMultipleFromTemplate() {
        return allowMultipleFromTemplate;
    }

    public void setAllowMultipleFromTemplate(final boolean allowMultipleFromTemplate) {
        this.allowMultipleFromTemplate = allowMultipleFromTemplate;
    }
}
