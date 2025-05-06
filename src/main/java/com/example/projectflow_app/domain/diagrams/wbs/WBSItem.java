package com.example.projectflow_app.domain.diagrams.wbs;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "wbs_items")
public class WBSItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;
    private String description;
    private String responsible;

    @ManyToOne
    @JoinColumn(name = "diagram_id")
    private WBSDiagram diagram;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private WBSItem parent;

    @OneToMany(mappedBy = "parent")
    private List<WBSItem> children = new ArrayList<>();

}
