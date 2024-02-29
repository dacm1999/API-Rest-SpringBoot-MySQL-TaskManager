package com.dacm.taskManager.responses;

import com.dacm.taskManager.dto.TasksDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TasksByUsernameResponse <T> {

    private String username;
    private int totalTasks;
    private int totalCompletedTasks;
    private int totalPendingTasks;
    private List<TasksDTO> completedTasks;
    private List<TasksDTO> pendingTasks;
    private List<TasksDTO> tasks;

    private long totalElements;
    private int totalPages;
    private int numberOfElements;
    private int size;
    private int page;

    public TasksByUsernameResponse() {
    }

    public TasksByUsernameResponse(String username, int totalTasks, int tasksCompleted, int tasksPendienting, List<TasksDTO> completedTasks, List<TasksDTO> pendientingTasks) {
        this.username = username;
        this.totalTasks = totalTasks;
        this.totalCompletedTasks = tasksCompleted;
        this.totalPendingTasks = tasksPendienting;
        this.completedTasks = completedTasks;
        this.pendingTasks = pendientingTasks;
    }

}
