package com.example.todoappV2.logic;

import com.example.todoappV2.TaskConfigurationProperties;
import com.example.todoappV2.model.Project;
import com.example.todoappV2.model.ProjectRepository;
import com.example.todoappV2.model.TaskGroupRepository;
import com.example.todoappV2.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogicConfiguration {

    @Bean
    ProjectService projectService(
            final ProjectRepository repository,
            final TaskGroupRepository taskGroupRepository,
            final TaskConfigurationProperties config){

        return new ProjectService(repository,
                                    taskGroupRepository,
                                    config);
    }

    @Bean
    TaskGroupService taskGroupService(final TaskGroupRepository taskGroupRepository,
                                      final TaskRepository taskRepository){
        return new TaskGroupService(taskGroupRepository,
                taskRepository);
    }
}
