
package com.projectmanagement.ProjectManagement.repository;


import com.projectmanagement.ProjectManagement.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

}
