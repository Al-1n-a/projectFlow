package com.example.projectflow_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiagramTypeDTO {
    private String type;          // Системное имя ("DFD")
    private String displayName;    // Читаемое имя ("Диаграмма потока данных")
}