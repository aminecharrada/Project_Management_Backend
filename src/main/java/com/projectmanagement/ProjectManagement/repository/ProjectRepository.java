package com.projectmanagement.ProjectManagement.repository;

import com.projectmanagement.ProjectManagement.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {


    @Query("SELECT p FROM Project p WHERE p.title = :title")
    Project findByTitle(@Param("title") String title);
}
