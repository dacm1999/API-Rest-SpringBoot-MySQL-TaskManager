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


    Page<Tasks> findAll(Specification<Tasks> specification, Pageable pageable);

    @Query("SELECT t FROM Tasks t WHERE t.user_id = ?1")
    List<Tasks> findByUserId(int userId);

}
