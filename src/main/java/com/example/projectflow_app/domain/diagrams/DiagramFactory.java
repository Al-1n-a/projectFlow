package com.example.projectflow_app.domain.diagrams;

import com.example.projectflow_app.dao.BoardRepository;
import com.example.projectflow_app.dao.DiagramRepository;
import com.example.projectflow_app.domain.Board;
import com.example.projectflow_app.domain.Type;
import com.example.projectflow_app.domain.diagrams.common.Diagram;
import com.example.projectflow_app.domain.diagrams.dfd.DFDDiagram;
import com.example.projectflow_app.domain.diagrams.gantt.GantDiagram;
import com.example.projectflow_app.service.BoardService;
import com.example.projectflow_app.service.UserService;
import jakarta.persistence.DiscriminatorValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DiagramFactory {
    private final BoardService boardService;

    @Autowired
    public DiagramFactory(BoardService boardService) {
        this.boardService = boardService;
    }

    public Diagram createDiagram(Type type, Long boardId) {
        Board board = boardService.findById(boardId);

        Diagram diagram = switch (type) {
            case GANTT -> GantDiagram.builder()
                    .board(board)
                    .title("New Gantt Chart")
                    .positionX(50)
                    .positionY(50)
                    .width(400)
                    .height(300)
                    .config("{}")
                    .build();

            case DFD -> DFDDiagram.builder()
                    .board(board)
                    .title("New DFD Diagram")
                    .positionX(50)
                    .positionY(50)
                    .width(400)
                    .height(300)
                    .config("{}")
                    .build();

            // Раскомментируйте по мере реализации других типов
            /*
            case WBS -> WBSDiagram.builder()
                    .board(board)
                    .title("New WBS Diagram")
                    .positionX(50)
                    .positionY(50)
                    .width(400)
                    .height(300)
                    .config("{}")
                    .build();
            */

            default -> throw new IllegalArgumentException("Unknown diagram type: " + type);
        };

        // Убедимся, что discriminator value будет корректным
        validateDiscriminatorValue(diagram);
        return diagram;
    }

    private void validateDiscriminatorValue(Diagram diagram) {
        DiscriminatorValue discriminator = diagram.getClass()
                .getAnnotation(DiscriminatorValue.class);

        if (discriminator == null) {
            throw new IllegalStateException("Diagram class " + diagram.getClass().getSimpleName() +
                    " must have @DiscriminatorValue annotation");
        }
    }
}