package com.dacm.taskManager.repository;

import com.dacm.taskManager.entity.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, Integer> {
    Tasks findByUser_id(int id);
}
