package com.example.todoappV2.controller;


import com.example.todoappV2.logic.TaskGroupService;
import com.example.todoappV2.model.Task;
import com.example.todoappV2.model.TaskGroup;
import com.example.todoappV2.model.TaskGroupRepository;
import com.example.todoappV2.model.TaskRepository;
import com.example.todoappV2.model.projection.GroupReadModel;
import com.example.todoappV2.model.projection.GroupWriteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(("/groups"))
public class TaskGroupController {

    private static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);
    private final TaskGroupService taskGroupService;
    private final TaskRepository repository;

    public TaskGroupController(TaskGroupService taskGroupService, TaskRepository repository) {
        this.taskGroupService = taskGroupService;
        this.repository = repository;
    }

    @PostMapping
    ResponseEntity<GroupReadModel> createNewGroup(@RequestBody @Valid GroupWriteModel toCreate){
        GroupReadModel result = taskGroupService.createGroup(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping
    ResponseEntity<List<GroupReadModel>> readAllGroups(){
        return ResponseEntity.ok(taskGroupService.readAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id){
        return ResponseEntity.ok(repository.findAllByGroup_Id(id));
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleGroup(@PathVariable int id){
        taskGroupService.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

    //Wszystkie exceptiony w obrebie controllera zostaną obsłużone w poniższy sposob
    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<?> handleIllegalArgument(IllegalArgumentException e){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<?> handleIllegalState(IllegalStateException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
