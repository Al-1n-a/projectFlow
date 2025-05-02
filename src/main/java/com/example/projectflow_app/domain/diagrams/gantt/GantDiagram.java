package com.example.projectflow_app.domain.diagrams.gantt;

import com.example.projectflow_app.domain.diagrams.common.Diagram;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder(toBuilder = true)
@DiscriminatorValue("GANTT")
public class GantDiagram extends Diagram {
    @OneToMany(mappedBy = "diagram", cascade = CascadeType.ALL)
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
}

