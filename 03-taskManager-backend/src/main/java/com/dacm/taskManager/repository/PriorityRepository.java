package com.dacm.taskManager.repository;

import com.dacm.taskManager.entity.Priorities;
import com.dacm.taskManager.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriorityRepository extends JpaRepository<Priorities, Integer> {

    Priorities findByName(String name);
    Page<Priorities> findAll(Specification<Priorities> specification, Pageable pageable);

}
