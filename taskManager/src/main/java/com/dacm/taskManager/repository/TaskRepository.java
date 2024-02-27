package com.dacm.taskManager.repository;

import com.dacm.taskManager.entity.Tasks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Tasks, Integer> {

//    @Query("SELECT users.firstname, users.lastname, users.username, tasks.name, tasks.description, tasks.status, priorities.name, tasks.creation_date, tasks.due_date) " +
//            "FROM User users " +
//            "INNER JOIN Tasks tasks ON users.userId = tasks.user_id " +
//            "INNER JOIN Priorities priorities ON priorities.id = tasks.priority_id " +
//            "WHERE users.username = :username")
//    List<TasksDTO> findTasksWithDetailsByUsername(@Param("username") String username);

    Page<Tasks> findAll(Specification<Tasks> specification, Pageable pageable);

    @Query("SELECT t FROM Tasks t WHERE t.user_id = ?1")
    List<Tasks> findByUserId(int userId);


}
