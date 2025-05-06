package com.example.projectflow_app.domain.diagrams.dfd;

import com.example.projectflow_app.domain.diagrams.common.Diagram;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@SuperBuilder(toBuilder = true)
@Table(name = "dfd_diagrams")
@DiscriminatorValue("DFD")
public class DFDDiagram extends Diagram {

    private Integer level;

    @OneToMany(mappedBy = "diagram", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DFDElement> elements = new ArrayList<>();

    public DFDDiagram() {}

    @Override
    public String getDefaultConfig() {
        return "{\"elements\": [], \"flows\": []}";
    }

    @Override
    public String getDiagramTypeName() {
        return "DFD";
    }
}
