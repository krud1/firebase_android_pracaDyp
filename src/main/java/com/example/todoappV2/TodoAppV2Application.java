package com.example.todoappV2;

import com.example.todoappV2.model.Task;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;

@EnableAsync
@SpringBootApplication
public class TodoAppV2Application {
	List<String> abc = new ArrayList<>();


	public static void main(String[] args) {
		SpringApplication.run(TodoAppV2Application.class, args);


	}

	@Bean
	Validator validator() {
		return new LocalValidatorFactoryBean();
	}



}
