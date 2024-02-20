package com.dacm.taskManager.repository;

import com.dacm.taskManager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    User findFirstByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsernameContainsIgnoreCase(String username);
    @Modifying()
    @Query("update User u set u.firstname=:firstname, u.lastname=:lastname, u.email=:country where u.userId = :id")
    void updateUser(@Param(value = "id") Integer id, @Param(value = "firstname") String firstname, @Param(value = "lastname") String lastname , @Param(value = "country") String country);
}
