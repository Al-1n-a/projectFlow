package com.example.projectflow_app.domain.diagrams.common;

import com.example.projectflow_app.domain.Board;
import com.example.projectflow_app.domain.Type;
import com.example.projectflow_app.domain.diagrams.common.Position;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import com.vladmihalcea.hibernate.type.json.JsonType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.type.SqlTypes;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "diagram_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "diagrams")
@Access(AccessType.FIELD)
public abstract class Diagram {
    private static final String SEQ_DIAGRAM = "diagram_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_DIAGRAM)
    @SequenceGenerator(name = SEQ_DIAGRAM, sequenceName = SEQ_DIAGRAM, allocationSize = 1)
    private Long id;
    private String title;
    private Integer positionX;
    private Integer positionY;
    private Integer width = 300;
    private Integer height = 400;

    //@Enumerated(EnumType.STRING) //не нужно, т.к. поле определено в @DiscriminatorColumn
    //private Type type;
    @Transient
    public Type getType() {
        String className = this.getClass().getSimpleName();
        return Type.valueOf(className.replace("Diagram", "").toUpperCase());
    }
    public abstract String getDiagramTypeName();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String config = "{}";;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void setPosition(Position position) {
        this.positionX = position.getX();
        this.positionY = position.getY();
        this.width = position.getWidth();
        this.height = position.getHeight();
    }

    @Transient
    public abstract String getDefaultConfig(); // Возвращает JSON-конфиг по умолчанию
}
