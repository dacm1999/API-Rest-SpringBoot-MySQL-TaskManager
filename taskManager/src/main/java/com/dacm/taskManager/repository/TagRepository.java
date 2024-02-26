package com.dacm.taskManager.repository;

import com.dacm.taskManager.entity.Tags;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TagRepository extends JpaRepository<Tags, Integer> {

    Tags findByName(String name);
    Page<Tags> findAll(Specification<Tags> specification, Pageable pageable);
}
