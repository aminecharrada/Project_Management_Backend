    package com.projectmanagement.ProjectManagement.entity;

    import com.fasterxml.jackson.annotation.JsonIgnore;
    import jakarta.persistence.*;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.time.LocalDate;
    import java.time.temporal.ChronoUnit;
    import java.util.List;

    @Data
    @NoArgsConstructor
    @Entity
    @Table(name = "project")
    public class Project {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String title;
        private String description;
        private double progress;
        @Column(name = "retard_percent")
        private double retardPercent;
        private String responsableName;
        private String responsableImage;

        private LocalDate startDate;
        private LocalDate endDate;

        @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        @JsonIgnore
        private List<Task> tasks;

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }
        public double getProgress() {
            return progress;
        }

        public void setProgress(double progress) {
            this.progress = progress;
            this.retardPercent = calculateRetardPercent();
        }

        public double calculateRetardPercent() {
            LocalDate today = LocalDate.now();

            // Check if startDate or endDate is null
            if (startDate == null || endDate == null) {
                return 0.0; // No delay if dates are not properly set
            }

            // Ensure the current date is within the project duration
            if (today.isBefore(startDate) || today.isAfter(endDate)) {
                return 0.0; // No retard if project hasn't started or has ended
            }

            // Calculate total duration and days passed
            long totalDays = ChronoUnit.DAYS.between(startDate, endDate);
            long daysPassed = ChronoUnit.DAYS.between(startDate, today);

            // Avoid division by zero if the project duration is 0 days
            if (totalDays == 0) {
                return 0.0;
            }

            // Calculate expected progress based on days passed
            double expectedProgress = (double) daysPassed / totalDays * 100;


            if (progress < expectedProgress) {
                return ((expectedProgress - progress) / expectedProgress) * 100;
            }

            return 0.0;
        }


        // Getter and setter for retardPercent
        public double getRetardPercent() {
            return retardPercent;
        }

        public void setRetardPercent(double retardPercent) {
            this.retardPercent = retardPercent;
        }

        @Override
        public String toString() {
            return "Project{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", progress=" + progress +
                    ", retardPercent=" + retardPercent +
                    ", responsableName='" + responsableName + '\'' +
                    ", startDate=" + startDate +
                    ", endDate=" + endDate +
                    '}';
        }
    }
