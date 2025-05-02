package com.example.projectflow_app.service;

import com.example.projectflow_app.dao.BoardRepository;
import com.example.projectflow_app.dao.DiagramRepository;
import com.example.projectflow_app.domain.Board;
import com.example.projectflow_app.domain.diagrams.common.Diagram;
import com.example.projectflow_app.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final DiagramRepository diagramRepository;
    private final UserService userService;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository, DiagramRepository diagramRepository, UserService userService) {
        this.boardRepository = boardRepository;
        this.diagramRepository = diagramRepository;
        this.userService = userService;
    }

    @Override
    public Board findById(Long id) {
        return boardRepository.findFirstById(id);
    }

    @Override
    @Transactional
    public Board createBoard(User user, String title, String description, List<Long> diagramIds) {
        Board board = new Board();
        board.setUser(user);
        board.setTitle(title);
        board.setDescription(description);
        List<Diagram> diagramList = getCollectRefDiagramsByIds(diagramIds);
        board.setDiagrams(diagramList);
        return boardRepository.save(board);
    }

    //это нахуй мне нужно??
    private List<Diagram> getCollectRefDiagramsByIds(List<Long> diagramIds) {
        return diagramIds.stream()
                //getOne вытаскивает ссылку на объект, findById - вытаскивает сам объект
                .map(diagramRepository::getOne)
                .collect(Collectors.toList());
    }

    @Override
    public void addDiagram(Board board, List<Long> diagramIds) {
        List<Diagram> diagrams = board.getDiagrams();
        List<Diagram> newDiagramList = diagrams == null ? new ArrayList<>() : new ArrayList<>(diagrams);
        newDiagramList.addAll(getCollectRefDiagramsByIds(diagramIds));
        board.setDiagrams(newDiagramList);
        boardRepository.save(board);
    }

    @Override
    @Transactional
    public Board getUserBoard(User user, Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Доска не найдена: " + boardId));
    }

    @Override
    @Transactional
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        boardRepository.delete(board);
    }

    @Override
    public boolean isBoardOwnedByUser(Long boardId, Long userId) {
        return boardRepository.existsByIdAndUserId(boardId, userId);
    }

    @Override
    public boolean isDiagramInBoard(Long boardId, Long diagramId) {
        return boardRepository.existsByIdAndDiagramsId(boardId, diagramId);
    }

}
