package com.example.projectflow_app.domain.diagrams.dfd;

import com.example.projectflow_app.domain.diagrams.common.Diagram;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "dfd_elements")
@Data
public class DFDElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String name; //"process", "dataStore", "externalEntity", "dataFlow"
//    private Integer positionX;
//    private Integer positionY;
    private String position; // JSON с координатами
    private String properties; // Дополнительные свойства в JSON

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diagram_id")
    private DFDDiagram diagram;
}
