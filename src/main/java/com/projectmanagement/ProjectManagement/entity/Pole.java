package com.projectmanagement.ProjectManagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name = "pole")
public class Pole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pole_name")
    private String poleName;

    @Column(name = "elem_pole")
    private String elemPole;

    @Column(name = "elem_pole_image")
    private String elemPoleImage;

    @Column(name = "pole_progress")
    private Double poleProgress;

    @Override
    public String toString() {
        return "Pole{" +
                "id=" + id +
                ", poleName='" + poleName + '\'' +
                ", elemPole='" + elemPole + '\'' +
                ", elemPoleImage='" + elemPoleImage + '\'' +
                ", poleProgress=" + poleProgress +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pole pole = (Pole) o;
        return Objects.equals(id, pole.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
