package com.example.todoappV2.logic;

import com.example.todoappV2.TaskConfigurationProperties;
import com.example.todoappV2.model.*;
import com.example.todoappV2.model.projection.GroupReadModel;
import com.example.todoappV2.model.projection.ProjectWriteModel;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//@Service
@RequestScope
public class ProjectService {

    private ProjectRepository projectRepository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfigurationProperties taskConfigurationProperties;

    public ProjectService(ProjectRepository projectRepository,
                          TaskGroupRepository taskGroupRepository,
                          TaskConfigurationProperties taskConfigurationProperties) {
        this.projectRepository = projectRepository;
        this.taskGroupRepository = taskGroupRepository;
        this.taskConfigurationProperties = taskConfigurationProperties;
    }

    public List<Project> readAll(){
        return projectRepository.findAll();
    }

    public Project save(final ProjectWriteModel toSave){
        return projectRepository.save(toSave.toProject());
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {
        if (!taskConfigurationProperties.getTemplate().isAllowMultipleTasks() &&
                taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }

        TaskGroup result = projectRepository.findById(projectId)
                .map(project -> {
                    var targetGroup = new TaskGroup();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(project.getSteps().stream()
                            .map(step ->
                                    new Task(step.getDescription(),
                                            deadline.plusDays(step.getDaysToDeadline())))
                            .collect(Collectors.toSet()));
                    targetGroup.setProject(project);
                    return taskGroupRepository.save(targetGroup, project);
                }).orElseThrow(()-> new IllegalArgumentException("Project with given id not found"));
        return new GroupReadModel(result);
    }
}
