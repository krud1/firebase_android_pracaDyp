package com.example.todoappV2.controller;

import com.example.todoappV2.TaskConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    private final DataSourceProperties dataSource;
    private final TaskConfigurationProperties myProp;

    public InfoController(DataSourceProperties dataSource, TaskConfigurationProperties myProp) {
        this.dataSource = dataSource;
        this.myProp = myProp;
    }

    @GetMapping("info/url")
    String url(){
        return dataSource.getUrl();
    }

    @GetMapping("info/prop")
    boolean myProp(){
        return myProp.getTemplate().isAllowMultipleTasks();
    }

}
