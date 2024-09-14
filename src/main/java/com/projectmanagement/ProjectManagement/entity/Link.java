    package com.projectmanagement.ProjectManagement.entity;

    import com.fasterxml.jackson.annotation.JsonProperty;
    import jakarta.persistence.*;


    @Entity
    @Table(name = "links")
    public class Link {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Version
        private Long version = 0L;

        @Column(name = "source_task_id")
        @JsonProperty("source")
        private Long sourceTaskId;

        @Column(name = "target_task_id")
        @JsonProperty("target")
        private Long targetTaskId;

        @Column(name = "type")
        private String type;

        // Getters and setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getSourceTaskId() {
            return sourceTaskId;
        }

        public void setSourceTaskId(Long sourceTaskId) {
            this.sourceTaskId = sourceTaskId;
        }

        public Long getTargetTaskId() {
            return targetTaskId;
        }

        public void setTargetTaskId(Long targetTaskId) {
            this.targetTaskId = targetTaskId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Long getVersion() {
            return version;
        }

        public void setVersion(Long version) {
            this.version = version;
        }
    }
