package com.example.todoappV2.adapter;

import com.example.todoappV2.model.Task;
import com.example.todoappV2.model.TaskRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {

    //@Override
    //boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);

}
