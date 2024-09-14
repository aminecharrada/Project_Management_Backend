    package com.projectmanagement.ProjectManagement.entity;

    import com.fasterxml.jackson.annotation.JsonBackReference;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.util.HashSet;
    import java.util.Objects;
    import java.util.Set;

    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    @Entity
    @Table(name = "personne")
    public class Personne {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "name")
        private String name;

        @Column(name = "image")
        private String image;

        @Column(name = "role")
        private String role;

        @Column(name = "poleName")
        private String poleName;

        @ManyToMany(mappedBy = "personnes")
        @JsonBackReference
        private Set<Task> tasks = new HashSet<>();

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Personne personne = (Personne) o;
            return Objects.equals(id, personne.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
