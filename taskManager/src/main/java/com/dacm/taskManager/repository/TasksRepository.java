package com.dacm.taskManager.repository;

import com.dacm.taskManager.dto.TasksDTO;
import com.dacm.taskManager.entity.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TasksRepository  extends JpaRepository<Tasks, Integer> {
//    Tasks findByUser_id(int id);

//    @Query("SELECT (users.firstname, users.lastname, users.username, tasks.name, tasks.description, tasks.status, priorities.name, CONCAT(FORMAT(tasks.creation_date, 'yyyy-MM-dd'), ' ', FORMAT(tasks.creation_date, 'HH:mm:ss')), tasks.due_date) " +
//            "FROM User users " +
//            "INNER JOIN Tasks tasks ON users.userId = tasks.user_id " +
//            "INNER JOIN Priorities priorities ON priorities.id = tasks.priority_id")
//    List<TasksDTO> getAllTasksWithDetails();


    @Query("SELECT t FROM Tasks t WHERE t.user_id = ?1")
    List<Task> findByUserId(int userId);


}
