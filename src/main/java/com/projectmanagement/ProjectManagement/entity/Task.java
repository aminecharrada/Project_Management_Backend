package com.projectmanagement.ProjectManagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "task")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String text;
    private String ressource;
    private String start_date;
    private Float progress;
    private Integer duration;
    private Integer parent;
    private String poleName;

    @ManyToMany
    @JoinTable(
            name = "task_personne",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "personne_id")
    )
    private Set<Personne> personnes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Project project;

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", ressource='" + ressource + '\'' +
                ", startDate='" + start_date + '\'' +
                ", progress=" + progress +
                ", duration=" + duration +
                ", parent=" + parent +
                ", poleName='" + poleName + '\'' +
                '}';
    }
}
