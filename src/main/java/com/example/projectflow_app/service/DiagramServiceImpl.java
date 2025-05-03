package com.example.projectflow_app.service;

import com.example.projectflow_app.dao.DiagramRepository;
import com.example.projectflow_app.domain.diagrams.DiagramFactory;
import com.example.projectflow_app.domain.Board;
import com.example.projectflow_app.domain.diagrams.common.Diagram;
import com.example.projectflow_app.domain.Type;
import com.example.projectflow_app.domain.User;
import com.example.projectflow_app.domain.diagrams.common.Position;
import com.example.projectflow_app.domain.diagrams.dfd.DFDDiagram;
import com.example.projectflow_app.domain.diagrams.gantt.GantDiagram;
import com.example.projectflow_app.dto.*;
import com.example.projectflow_app.mapper.DiagramMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.IOException;

@Service
@Transactional
public class DiagramServiceImpl implements DiagramService {

    private final DiagramMapper diagramMapper;
    private final ObjectMapper objectMapper; // Для работы с JSON

    private final DiagramRepository diagramRepository;
    private final UserService userService;
    private final BoardService boardService;
    private final DiagramFactory diagramFactory;

    @Autowired
    public DiagramServiceImpl(DiagramRepository diagramRepository, DiagramMapper diagramMapper, ObjectMapper objectMapper, UserService userService, BoardService boardService, DiagramFactory diagramFactory) {
        this.diagramRepository = diagramRepository;
        this.diagramMapper = diagramMapper;
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.boardService = boardService;
        this.diagramFactory = diagramFactory;
    }

    @Override
    public List<DiagramDTO> getAllUsersDiagrams(String username) {
        User user = userService.findWithBoardsAndDiagramsByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found" + username);
        }
        //return mapper.fromDiagramList(diagramRepository.findAll());

//        return user.getBoards().stream()
//                .flatMap(board -> board.getDiagrams().stream())
//                .map(mapper::fromDiagram)
//                .collect(Collectors.toList());

        List<DiagramDTO> result = new ArrayList<>();

        // Для каждой доски пользователя
        for (Board board : user.getBoards()) {
            // Для каждой диаграммы на доске
            for (Diagram diagram : board.getDiagrams()) {
                // Преобразуем и добавляем в результат
                result.add(diagramMapper.toDTO(diagram));
            }
        }
        return result;
    }

    @Override
    public List<DiagramTypeDTO> getAvailableDiagramTypes() {
        //return Arrays.asList(Type.values());
        return Arrays.stream(Type.values())
                .map(type -> new DiagramTypeDTO(
                        type.name(),
                        type.getDisplayName()))
                .collect(Collectors.toList());
    }

    @Override
    public DiagramDTO createDiagram(Long boardId, String diagramType, String username) {
        Board board = boardService.findById(boardId);
        User user = userService.findByUsername(username);

        if (!board.getUser().equals(user)) {
            throw new SecurityException("User does not own the board");
        }

        Type type;
        try {
            type = Type.valueOf(diagramType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid diagram type: " + diagramType);
        }

        Diagram diagram = diagramFactory.createDiagram(type, board.getId());
        diagram.setBoard(board);
        return diagramMapper.toDTO(diagramRepository.save(diagram));
    }

    @Override
    @Transactional
    public DiagramDTO updateDiagramConfig(Long diagramId, String configJson) {
        Diagram diagram = diagramRepository.findById(diagramId).orElseThrow();
        validateConfig(diagram.getType(), configJson);
        diagram.setConfig(configJson);
        return diagramMapper.toDTO(diagramRepository.save(diagram));
    }

    private void validateConfig(Type type, String configJson) {
        try {
            JsonNode root = objectMapper.readTree(configJson);
            switch (type) {
                case GANTT:
                    if (!root.has("tasks")) throw new IllegalArgumentException("Missing 'tasks'");
                    break;
                case DFD:
                    if (!root.has("elements")) throw new IllegalArgumentException("Missing 'elements'");
                    break;
                // ... другие типы
            }
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid JSON format");
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read JSON");
        }
    }

    @Override
    @Transactional
    public DiagramDTO updateDiagram(Long diagramId, DiagramUpdateDTO updateDTO, String username) {
        Diagram diagram = diagramRepository.findById(diagramId)
                .orElseThrow(() -> new RuntimeException("Diagram not found with id: " + diagramId));
        User user = userService.findByUsername(username);
        if (!diagram.getBoard().getUser().equals(user)) {
            throw new SecurityException("User does not own the diagram");
        }

        if (updateDTO.getTitle() != null) {
            diagram.setTitle(updateDTO.getTitle());
        }

        if (updateDTO.getPositionX() != null) {
            diagram.setPositionX(updateDTO.getPositionX());
        }

        if (updateDTO.getPositionY() != null) {
            diagram.setPositionY(updateDTO.getPositionY());
        }

        if (updateDTO.getWidth() != null) {
            diagram.setWidth(updateDTO.getWidth());
        }

        if (updateDTO.getHeight() != null) {
            diagram.setHeight(updateDTO.getHeight());
        }

        if (updateDTO.getConfig() != null) {
            updateDiagramSpecificData(diagram, updateDTO.getConfig());
        }

        return diagramMapper.toDTO(diagramRepository.save(diagram));
    }

    private void updateDiagramSpecificData(Diagram diagram, String configJson) {
        try {
            JsonNode root = objectMapper.readTree(configJson);
            switch (diagram.getType()) {
                case GANTT:
                    ((GantDiagram) diagram).setConfig(configJson);
                    break;
                case DFD:
                    ((DFDDiagram) diagram).setConfig(configJson);
                    break;
                // ... другие типы
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid config JSON");
        }
    }

    private Position calculateNewPosition(Board board) {
        int x = 50;  //стартовые координаты
        int y = 50;

        if (!board.getDiagrams().isEmpty()) {
            Diagram lastDiagram = board.getDiagrams().get(board.getDiagrams().size() - 1);
            x = lastDiagram.getPositionX() + lastDiagram.getWidth() + 30; // смещение вправо
            y = lastDiagram.getPositionY(); // сохраняем ту же вертикальную позицию
        }

        return new Position(x, y, 400, 300); // Ширина и высота по умолчанию
    }

    @Override
    public void updateDiagramPosition(Long diagramId, PositionDTO newPosition) {
        Diagram diagram = diagramRepository.findById(diagramId)
                .orElseThrow(() -> new RuntimeException("Diagram not found: " + diagramId));

        diagram.setPositionX(newPosition.getX());
        diagram.setPositionY(newPosition.getY());
        diagram.setWidth(newPosition.getWidth());
        diagram.setHeight(newPosition.getHeight());

        diagramRepository.save(diagram);
    }

    @Override
    @Transactional
    public void deleteDiagram(Long diagramId, Long boardId, String username) {
        // Загружаем диаграмму вместе с доской и пользователем
        Diagram diagram = diagramRepository.findById(diagramId)
                .orElseThrow(() -> new RuntimeException("Diagram not found"));

        // Проверяем принадлежность к доске
        if (!diagram.getBoard().getId().equals(boardId)) {
            throw new RuntimeException("Diagram does not belong to board");
        }

        // Проверяем владельца
        User user = userService.findByUsername(username);
        if (!diagram.getBoard().getUser().getId().equals(user.getId())) {
            throw new SecurityException("User doesn't own this diagram");
        }

        // Явное удаление с очисткой связей
        diagram.getBoard().getDiagrams().remove(diagram);
        diagramRepository.delete(diagram);
        diagramRepository.flush();
    }
}
