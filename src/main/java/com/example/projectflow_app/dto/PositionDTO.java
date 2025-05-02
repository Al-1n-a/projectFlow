package com.example.projectflow_app.dto;

import lombok.Data;

@Data
public class PositionDTO {
    //@Min(0)
    private int x;

    //@Min(0)
    private int y;

    //@Min(100)
    private int width;

    //@Min(100)
    private int height;
}
