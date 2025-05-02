package com.example.projectflow_app.dao;

import com.example.projectflow_app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findFirstByName(String name);

    // Оптимизированный метод с JOIN FETCH
    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN FETCH u.boards b " +
            "LEFT JOIN FETCH b.diagrams " +
            "WHERE u.name = :username")
    Optional<User> findWithBoardsAndDiagramsByUsername(@Param("username") String username);

    boolean existsByName(String name);
}
