package com.example.todoappV2.logic;

import com.example.todoappV2.TaskConfigurationProperties;
import com.example.todoappV2.model.Project;
import com.example.todoappV2.model.ProjectRepository;
import com.example.todoappV2.model.TaskGroup;
import com.example.todoappV2.model.TaskGroupRepository;
import com.example.todoappV2.model.projection.GroupReadModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("should throw ISE when configured to allow just 1 group and undone group exists")
    void createGroup_noMultipleGroupsConfig_And_undoneGroupsExist_throwsIllegalStateException() {
        //given
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt()))
                .thenReturn(true);
        TaskConfigurationProperties mockConfig = configurationReturning(false);
        //system under test
        var toTest = new ProjectService(null, mockGroupRepository, mockConfig);
        //when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("one undone group");
    }

    @Test
    @DisplayName("should throw IAE when configuration ok and no projects for a given id")
    void createGroup_configOk_And_noProjects_throwsIllegalArgumentException() {
        //given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        //system under test
        var toTest = new ProjectService(mockRepository, null, mockConfig);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }


    @Test
    @DisplayName("should throw IAE when configured to allow 1 group and no groups and no projects for a given id")
    void createGroup_noMultipleGroupsConfig_And_noUndoneGroupExists_noProjects_throwsIllegalArgumentException() {
        //given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //and
        groupRepositoryReturning(false);
        //and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        //system under test
        var toTest = new ProjectService(mockRepository, null, mockConfig);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("Shoudl create a new group from project")
    void createGroup_configOk_existingProject_createsAndSavesGroup() {
        //given
        var today = LocalDate.now().atStartOfDay();
        //and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        //and
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //and
        TaskGroupRepository inMemoryGroupRepo = inMemoryGroupRepository();
        //sys under test
        var toTest = new ProjectService(mockRepository, inMemoryGroupRepo, mockConfig);
        //when
        GroupReadModel result = toTest.createGroup(today,1);
        //then
        //assertThat(result)



    }

    private TaskGroupRepository inMemoryGroupRepository() {
        return new TaskGroupRepository() {
            private int index = 0;
            private Map<Integer, TaskGroup> map = new HashMap<>();

            @Override
            public List<TaskGroup> findAll() {
                return new ArrayList<>(map.values());
            }

            @Override
            public Optional<TaskGroup> findById(Integer id) {
                return Optional.ofNullable(map.get(id));
            }

            @Override
            public TaskGroup save(TaskGroup entity, Project project) {
                if (entity.getId() == 0) {
                    try {
                        var field = TaskGroup.class.getDeclaredField("id");
                        field.setAccessible(true);
                        field.set(entity, ++index);
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        throw new RuntimeException(e);

                    }
                }
                map.put(entity.getId(), entity);
                return entity;
            }


            @Override
            public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
                return map.values().stream()
                        .filter(g -> !g.isDone())
                        .anyMatch(g -> g.getProject() != null && g.getProject().getId() == projectId);
            }
        };
    }




            private TaskGroupRepository groupRepositoryReturning(final boolean b) {
                var mockGroupRepository = mock(TaskGroupRepository.class);
                when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt()))
                        .thenReturn(b);
                return mockGroupRepository;
            }


            private TaskConfigurationProperties configurationReturning(final boolean b) {
                var mockTemplate = mock(TaskConfigurationProperties.Template.class);
                when(mockTemplate.isAllowMultipleTasks()).thenReturn(b);
                var mockConfig = mock(TaskConfigurationProperties.class);
                when(mockConfig.getTemplate()).thenReturn(mockTemplate);
                return mockConfig;
    }
}


