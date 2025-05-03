package com.example.projectflow_app.service;

import com.example.projectflow_app.domain.Board;
import com.example.projectflow_app.domain.User;

import java.util.List;

public interface BoardService {
    Board createBoard(User user, String title, String description, List<Long> diagramIds);

    void addDiagram(Board board, List<Long> diagramIds);

    Board findById(Long id);

    Board getUserBoard(User user, Long boardId);

    //void addToUserBoard(Long diagramId, String username, Long boardId);
    public void deleteBoard(Long boardId);

    public boolean isBoardOwnedByUser(Long boardId, Long userId);

    public boolean isDiagramInBoard(Long boardId, Long diagramId);

    public Board findBoardWithDiagrams(Long boardId);

}