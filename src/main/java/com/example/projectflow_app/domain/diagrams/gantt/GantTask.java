package com.example.projectflow_app.domain.diagrams.gantt;

import com.example.projectflow_app.domain.diagrams.common.Diagram;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "gantt_tasks")
@Data
public class GantTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double progress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diagram_id")
    private Diagram diagram;
}
