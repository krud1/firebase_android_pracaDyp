package com.example.todoappV2.model.projection;

import com.example.todoappV2.model.Project;
import com.example.todoappV2.model.Project_steps;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;

public class ProjectWriteModel {

    @NotBlank(message = "Project's desc must not be empty")
    private String description;
    @Valid
    private List<Project_steps> steps;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Project_steps> getSteps() {
        return steps;
    }

    public void setSteps(List<Project_steps> steps) {
        this.steps = steps;
    }

    public Project toProject(){
        var result = new Project();
        result.setDescription(description);
        steps.forEach(step -> step.setProject(result));
        result.setSteps(new HashSet<>(steps));
        return result;
    }
}
