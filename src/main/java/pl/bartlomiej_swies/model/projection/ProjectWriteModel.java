package pl.bartlomiej_swies.model.projection;

import pl.bartlomiej_swies.model.Project;
import pl.bartlomiej_swies.model.ProjectSteps;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ProjectWriteModel {

    @NotBlank(message = "Project's description must be not blank")
    private String description;

    @Valid
    private List<ProjectSteps> steps = new ArrayList<>();

    public ProjectWriteModel() {
        steps.add(new ProjectSteps());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public List<ProjectSteps> getSteps() {
        return steps;
    }

    public void setSteps(final List<ProjectSteps> steps) {
        this.steps = steps;
    }

    public Project toProject() {
        var result = new Project();
        result.setDescription(description);
        steps.forEach(step -> step.setProject(result));
        result.setSteps(new HashSet<>(steps));
        return result;
    }
}
