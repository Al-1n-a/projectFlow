package com.example.projectflow_app.domain.diagrams.gantt;

import com.example.projectflow_app.domain.diagrams.common.Diagram;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@SuperBuilder(toBuilder = true)
@Table(name = "gantt_diagrams")
@DiscriminatorValue("GANTT")
public class GantDiagram extends Diagram {
    //@Column(name = "start_date")
    private LocalDate projectStartDate;

    //@Column(name = "end_date")
    private LocalDate projectEndDate;

    @OneToMany(mappedBy = "diagram", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GantTask> tasks;

    public GantDiagram() {}

    @Override
    public String getDefaultConfig() {
        return """
        {
            "tasks": [],
            "startDate": "2023-01-01",
            "endDate": "2023-12-31"
        }
        """;
    }

    @Override
    public String getDiagramTypeName() {
        return "GANTT";
    }
}

