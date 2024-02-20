package com.dacm.taskManager.repository;

import com.dacm.taskManager.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TagRepository extends JpaRepository<Tags, Integer> {

    Tags findByName(String name);

}
