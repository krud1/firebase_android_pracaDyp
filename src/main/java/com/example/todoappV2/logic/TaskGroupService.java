package com.example.todoappV2.logic;

import com.example.todoappV2.model.Project;
import com.example.todoappV2.model.TaskGroup;
import com.example.todoappV2.model.TaskGroupRepository;
import com.example.todoappV2.model.TaskRepository;
import com.example.todoappV2.model.projection.GroupReadModel;
import com.example.todoappV2.model.projection.GroupWriteModel;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.stream.Collectors;

//@Service
//RequestScope - zapewnia unikalną instancję serwisu przy żądaniu
@RequestScope
public class TaskGroupService {

    private final TaskGroupRepository repository;
    private final TaskRepository taskRepository;

    public TaskGroupService(final TaskGroupRepository repository,
                            final TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source){
        return createGroup(source, null);
    }

    public GroupReadModel createGroup(final GroupWriteModel source, final Project project){
        TaskGroup result = repository.save(source.toGroup(project), project);
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll(){
        return repository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId){
        if(taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)) {
            throw new IllegalStateException("Group has undone tasks");
        }
        TaskGroup result = repository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("TaskGroup with given id not found"));
        result.setDone(!result.isDone());
        repository.save(result,null );
    }




}
