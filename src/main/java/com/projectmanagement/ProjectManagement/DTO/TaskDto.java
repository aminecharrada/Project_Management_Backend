    package com.projectmanagement.ProjectManagement.DTO;

    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;


    import java.util.Set;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class TaskDto {
        private Long id;
        private String text;
        private String ressource;
        private String start_date;
        private Float progress;
        private Integer duration;
        private Integer parent;
        private String poleName;
        private Set<Long> personnes;
    }
