package com.projectmanagement.ProjectManagement.repository;

import com.projectmanagement.ProjectManagement.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {


    @Query("SELECT t FROM Task t WHERE t.project.title = :projectName")
    List<Task> findTasksByProjectName(@Param("projectName") String projectName);


    @Query("SELECT t FROM Task t WHERE t.poleName = :poleName")
    List<Task> findByPoleName(@Param("poleName") String poleName);
//    @Query("SELECT t FROM Task t WHERE t.pole.id = :poleId")
//    List<Task> findByPoleId(@Param("poleId") Long poleId);
    @Query("SELECT t FROM Task t WHERE t.dureeReelle < CURRENT_DATE AND t.progress < 1")
    List<Task> findOverdueTasks();


}
