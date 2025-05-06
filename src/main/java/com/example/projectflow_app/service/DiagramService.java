package com.example.projectflow_app.service;

import com.example.projectflow_app.domain.Type;
import com.example.projectflow_app.domain.diagrams.common.Diagram;
import com.example.projectflow_app.dto.*;

import java.util.List;

public interface DiagramService {
    List<DiagramDTO> getAllUsersDiagrams(String username);

    //void addToUserBoard(Long diagramId, String username, Long boardId);

    List<DiagramTypeDTO> getAvailableDiagramTypes();

    DiagramDTO createDiagram(Long boardId, String diagramType, String username);

    void updateDiagramPosition(Long diagramId, PositionDTO newPosition);

    void deleteDiagram(Long diagramId, Long boardId, String username);

    DiagramDTO updateDiagram(Long diagramId, DiagramUpdateDTO updateDTO, String username);

    DiagramDTO updateDiagramConfig(Long diagramId, String configJson);

    Diagram findById(Long id);
    Diagram save(Diagram diagram);

    }
