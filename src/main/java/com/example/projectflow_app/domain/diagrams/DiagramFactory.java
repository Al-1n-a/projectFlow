package com.example.projectflow_app.domain.diagrams;

import com.example.projectflow_app.dao.BoardRepository;
import com.example.projectflow_app.dao.DiagramRepository;
import com.example.projectflow_app.domain.Type;
import com.example.projectflow_app.domain.diagrams.common.Diagram;
import com.example.projectflow_app.domain.diagrams.dfd.DFDDiagram;
import com.example.projectflow_app.domain.diagrams.gantt.GantDiagram;
import com.example.projectflow_app.service.BoardService;
import com.example.projectflow_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DiagramFactory {
    private final BoardRepository boardRepository;

    @Autowired
    public DiagramFactory(BoardRepository boardRepository, DiagramRepository diagramRepository, UserService userService, BoardService boardService) {
        this.boardRepository = boardRepository;
    }

    public Diagram createDiagram(Type type, Long boardId) {
        Diagram diagram;
        switch (type) {
            case GANTT:
                diagram = new GantDiagram();
                break;
            case DFD:
                diagram = new DFDDiagram();
                break;
//            case WBS:
//                diagram = new WBSDiagram();
//                break;
//            case SAM:
//                diagram = new SAMDiagram();
//                break;
//            case TIMELINE:
//                diagram = new TimelineDiagram();
//                break;
//            case CFD:
//                diagram = new CFDDiagram();
//                break;
//            case AGENDA:
//                diagram = new AGENDADiagram();
//                break;
//            case ISHIKAWA:
//                diagram = new IsikawaDiagram();
//                break;
//            case RT:
//                diagram = new RTDiagram();
//                break;
//            case STRATEGYMAP:
//                diagram = new StrategyMapDiagram();
//                break;
//            case AGILE:
//                diagram = new AGILEDiagram();
//                break;
            default:
                throw new IllegalArgumentException("Unknown diagram type: " + type);
        }
        diagram.setTitle("New Diagram");
        return diagram;
    }
}