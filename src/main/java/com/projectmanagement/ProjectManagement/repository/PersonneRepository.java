package com.projectmanagement.ProjectManagement.repository;

import com.projectmanagement.ProjectManagement.entity.Personne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonneRepository extends JpaRepository<Personne, Long> {
}