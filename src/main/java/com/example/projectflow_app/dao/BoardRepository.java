package com.example.projectflow_app.dao;

import com.example.projectflow_app.domain.Board;
import com.example.projectflow_app.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findFirstById(Long id);
    // Проверяет, что доска принадлежит пользователю
    boolean existsByIdAndUserId(Long boardId, Long userId);
    // Проверяет, что диаграмма принадлежит доске
    boolean existsByIdAndDiagramsId(Long boardId, Long diagramId);

    @EntityGraph(attributePaths = {"diagrams"})
    @Query("SELECT b FROM Board b WHERE b.id = :boardId")
    Optional<Board> findByIdWithDiagrams(@Param("boardId") Long boardId);

}
