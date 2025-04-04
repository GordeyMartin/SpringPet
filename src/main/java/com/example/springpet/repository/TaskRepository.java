package com.example.springpet.repository;

import com.example.springpet.model.Status;
import com.example.springpet.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Modifying
    @Query("UPDATE Task t SET t.name = :name WHERE t.id = :id ")
    void updateName(@Param("id") Long id, @Param("name") String name);

    @Modifying
    @Query("UPDATE Task t SET t.description = :description WHERE t.id = :id ")
    void updateDescription(@Param("id") Long id, @Param("description") String description);

    @Modifying
    @Query("UPDATE Task t SET t.deadline = :deadline WHERE t.id = :id ")
    void updateDeadline(@Param("id") Long id, @Param("deadline") LocalDate deadline);

    @Modifying
    @Query("UPDATE Task t SET t.status = :status WHERE t.id = :id ")
    void updateStatus(@Param("id") Long id, @Param("status") Status status);
}
