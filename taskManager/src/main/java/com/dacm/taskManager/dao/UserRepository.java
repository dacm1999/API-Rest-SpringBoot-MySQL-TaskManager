package com.dacm.taskManager.dao;

import com.dacm.taskManager.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Integer> {
}
