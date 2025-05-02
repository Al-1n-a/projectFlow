package com.example.projectflow_app.domain;

import com.example.projectflow_app.domain.diagrams.common.Diagram;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "boards")
public class Board {
    private static final String SEQ_BOARD = "board_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_BOARD)
    @SequenceGenerator(name = SEQ_BOARD, sequenceName = SEQ_BOARD, allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diagram> diagrams = new ArrayList<>();

    @Column(nullable = false) // title обязателен
    private String title;
    private String description; //для собрания 30 мая с отделом коммерции

    @CreationTimestamp //дата создания будет записываться в это поле
    private LocalDateTime created;

    @UpdateTimestamp//дата последнего изменения будет записываться в это поле
    private LocalDateTime modified;
}
