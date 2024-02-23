package com.dacm.taskManager.repository;

import com.dacm.taskManager.entity.Priorities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrioritiesRepository extends JpaRepository<Priorities, Integer> {

    Priorities findByName(String name);
}
