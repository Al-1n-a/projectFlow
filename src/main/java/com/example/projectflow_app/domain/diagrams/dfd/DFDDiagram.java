package com.example.projectflow_app.domain.diagrams.dfd;

import com.example.projectflow_app.domain.diagrams.common.Diagram;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@SuperBuilder(toBuilder = true)
@DiscriminatorValue("DFD")
public class DFDDiagram extends Diagram {
    @OneToMany(mappedBy = "diagram", cascade = CascadeType.ALL)
    private List<DFDElement> elements;

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
