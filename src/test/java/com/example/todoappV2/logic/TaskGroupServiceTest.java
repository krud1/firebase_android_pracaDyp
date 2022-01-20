package com.example.todoappV2.logic;

import com.example.todoappV2.model.TaskGroup;
import com.example.todoappV2.model.TaskGroupRepository;
import com.example.todoappV2.model.TaskRepository;
import com.example.todoappV2.model.projection.GroupReadModel;
import com.example.todoappV2.model.projection.GroupTaskWriteModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    @DisplayName("toggleGroup_whenExistsReturnTrue_should_throw_IllegalStateException")
    void toggleGroup_ExistsTrue_throws_IllegalStateException(){
        //given
        var mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(true);
        //system under test
        var toTest = new TaskGroupService(null, mockTaskRepository);
        //when
        var exception = catchThrowable(() -> toTest.toggleGroup(anyInt()));
        //then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("has undone tasks");
    }

    @Test
    @DisplayName("toggleGroup_whenExistsReturnFalse_and_findById_Returns_EmptyOptional_should_throw_IllegalArgumentException")
    void toggleGroup_findById_returns_emptyOptional_and_throws_IllegalArgumentException(){
        //given
        var mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(false);
        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.empty());
        //system under test
        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);
        //when
        var exception = catchThrowable(() -> toTest.toggleGroup(anyInt()));
        //then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("toggleGroup_whenExistsReturnFalse_and_findById_returns_TaskGroup_ok_should_!set.setDone_andSaveToRepo")
    void toggleGroup_allOk(){
        //given
        TaskGroup toToggle = new TaskGroup();
        toToggle.setDone(false);
        toToggle.setId(2);

        var mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(false);
        //and
        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.of(toToggle));
        //sys under test
        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);
        //when
        toTest.toggleGroup(anyInt());
        //then
        assertThat(toToggle.isDone()).isTrue();

    }






}