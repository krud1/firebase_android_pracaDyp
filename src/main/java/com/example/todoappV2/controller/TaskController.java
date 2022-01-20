package com.example.todoappV2.controller;


import com.example.todoappV2.logic.TaskService;
import com.example.todoappV2.model.Task;
import com.example.todoappV2.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@RestController
//sciezka do calej klasy
//@RequestMapping("/tasks")
public class TaskController {
    public static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskRepository repository;
    private final TaskService service;

    public TaskController(final TaskRepository repository, final TaskService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping(value = "/tasks", params = {"!sort", "!page", "!size"})
   ResponseEntity<List<Task>> readAllTasks(){
        logger.warn("ReadAllTasksNoParams");
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/tasks/done")
    ResponseEntity<List<Task>> readTasksByDone(@RequestParam boolean done) {
        logger.info("readTasksByDone");
        return ResponseEntity.ok(repository.findByDone(done));
    }

    @GetMapping("/tasks")
    ResponseEntity<List<Task>> readAllTasks(Pageable page){
        logger.warn("ReadAllTasks");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @GetMapping("/tasks/search/done")
    ResponseEntity<List<Task>> readDoneTasks(@RequestParam(defaultValue = "true") boolean state){
        return ResponseEntity.ok(
                repository.findByDone(state)
        );
    }


    @GetMapping("/tasks/{id}")
    ResponseEntity<Task> readTaskById(@PathVariable int id) {
        logger.warn("Find task by id");
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/tasks")
    ResponseEntity<Task> addNewTask(@RequestBody @Valid Task task){
        logger.info("Adding new task!");
        return ResponseEntity.ok(repository.save(task));
    }

    @Transactional
    @PutMapping("/tasks/{id}")
    ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate){
        if(!repository.existsById(id)){
            logger.info("Changing existing task - task not found");
            return ResponseEntity.notFound().build();
        }
        else {
            repository.findById(id)
                    .ifPresent(task -> {
                        task.updateFrom(toUpdate);
                        repository.save(task);
                    });

        }
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @DeleteMapping("/tasks/{id}")
    ResponseEntity<Task> deleteTaskById(@PathVariable int id){
        if(repository.existsById(id)){
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    @PatchMapping("/tasks/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id){
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task -> task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();
    }



}
