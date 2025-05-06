package com.example.projectflow_app.domain.diagrams.wbs;

import com.example.projectflow_app.domain.diagrams.common.Diagram;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@DiscriminatorValue("WBS")
@SuperBuilder(toBuilder = true)
@Table(name = "wbs_diagrams")
public class WBSDiagram extends Diagram {

    @OneToMany(mappedBy = "diagram", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WBSItem> items = new ArrayList<>();

    private String projectCode;

    public WBSDiagram() {    }

    @Override
    public String getDefaultConfig() {
        //надо изменить потом
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
        return "WBS";
    }
}
