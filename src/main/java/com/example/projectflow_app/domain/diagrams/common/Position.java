package com.example.projectflow_app.domain.diagrams.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Position {
    private int x;          // координата X (горизонталь)
    private int y;          // координата Y (вертикаль)
    private int width;      // ширина диаграммы
    private int height;     // высота диаграммы
}
