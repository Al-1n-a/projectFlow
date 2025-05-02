package com.example.projectflow_app.dao;

import com.example.projectflow_app.domain.diagrams.common.Diagram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DiagramRepository extends JpaRepository<Diagram, Long> {

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Diagram d WHERE d.id = :id")
    boolean existsById(@Param("id") Long id);
}
