package com.projectmanagement.ProjectManagement.repository;

import com.projectmanagement.ProjectManagement.entity.Pole;
import com.projectmanagement.ProjectManagement.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PoleRepository extends JpaRepository<Pole, Long> {


        @Query("SELECT t FROM Task t WHERE t.poleName = :poleName")
        List<Task> findTasksByPoleName(@Param("poleName") String poleName);
        Optional<Pole> findByPoleName(String poleName);
}
