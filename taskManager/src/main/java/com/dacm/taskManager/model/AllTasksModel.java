package com.dacm.taskManager.model;

import com.dacm.taskManager.dto.TagsDTO;
import com.dacm.taskManager.dto.TasksDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AllTasksModel {

    private String username;
    private String lastname;
    private String firstname;
    private int totalTasks;
    private int tasksCompleted;
    private int tasksPendienting;
    private List<TasksDTO> completedTasks;
    private List<TasksDTO> pendientingTasks;

    public AllTasksModel(String username, String lastname, String firstname, int totalTasks, int tasksCompleted, int tasksPendienting, List<TasksDTO> completedTasks, List<TasksDTO> pendientingTasks) {
        this.username = username;
        this.lastname = lastname;
        this.firstname = firstname;
        this.totalTasks = totalTasks;
        this.tasksCompleted = tasksCompleted;
        this.tasksPendienting = tasksPendienting;
        this.completedTasks = completedTasks;
        this.pendientingTasks = pendientingTasks;
    }
}
