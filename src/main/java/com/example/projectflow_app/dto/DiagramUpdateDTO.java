package com.example.projectflow_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DiagramUpdateDTO {
//    private Integer positionX;
//    private Integer positionY;
//    private Integer width;
//    private Integer height;
    private String config;
    private String title;
    private LocalDate startDate; // Для Gantt
    private LocalDate endDate;   // Для Gantt
    private Integer level;       // Для DFD
    private String projectCode;  // Для WBS
    // Другие специфичные поля
}