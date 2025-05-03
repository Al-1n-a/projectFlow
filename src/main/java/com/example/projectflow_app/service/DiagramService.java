package com.example.projectflow_app.service;

import com.example.projectflow_app.domain.Type;
import com.example.projectflow_app.dto.*;

import java.util.List;

public interface DiagramService {
    List<DiagramDTO> getAllUsersDiagrams(String username);

    //void addToUserBoard(Long diagramId, String username, Long boardId);

    List<DiagramTypeDTO> getAvailableDiagramTypes();

    public DiagramDTO createDiagram(Long boardId, String diagramType, String username);

    void updateDiagramPosition(Long diagramId, PositionDTO newPosition);

    public void deleteDiagram(Long diagramId, Long boardId, String username);

    public DiagramDTO updateDiagram(Long diagramId, DiagramUpdateDTO updateDTO, String username);

    public DiagramDTO updateDiagramConfig(Long diagramId, String configJson);

    }
