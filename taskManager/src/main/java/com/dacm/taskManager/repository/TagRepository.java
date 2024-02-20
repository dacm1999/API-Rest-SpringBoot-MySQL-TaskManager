package com.dacm.taskManager.repository;

import com.dacm.taskManager.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tags, Integer> {


}
