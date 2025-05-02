package com.example.projectflow_app.dto;

import com.example.projectflow_app.domain.Type;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class DiagramDTO {
    private Long id;
    private String title;
    private String type;
    private Integer x, y, width, height;
    private String config;  // JSON-строка

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
