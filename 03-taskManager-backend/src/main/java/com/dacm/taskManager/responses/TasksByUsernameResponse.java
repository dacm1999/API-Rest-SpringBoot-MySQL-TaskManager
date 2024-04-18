package com.dacm.taskManager.responses;

import com.dacm.taskManager.dto.TasksDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TasksByUsernameResponse {

    private String username;
    private int totalTasks;
    private int totalCompletedTasks;
    private int totalPendingTasks;
    private List<TasksDTO> completedTasks;
    private List<TasksDTO> pendingTasks;

    private int numberOfElements;
    private long totalElements;
    private int size;
    private int page;
    private int totalPages;

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

    public TasksByUsernameResponse(String username, int totalTasks, int totalCompletedTasks, int totalPendingTasks) {
        this.username = username;
        this.totalTasks = totalTasks;
        this.totalCompletedTasks = totalCompletedTasks;
        this.totalPendingTasks = totalPendingTasks;
    }

    public TasksByUsernameResponse(int numberOfElements, long totalElements, int size, int page, int totalPages) {
        this.numberOfElements = numberOfElements;
        this.totalElements = totalElements;
        this.size = size;
        this.page = page;
        this.totalPages = totalPages;
    }
}
