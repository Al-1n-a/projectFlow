package com.example.projectflow_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiagramUpdateDTO {
    private String title;
    private Integer positionX;
    private Integer positionY;
    private Integer width;
    private Integer height;
    private String config;
}