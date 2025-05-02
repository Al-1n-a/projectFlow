package com.example.projectflow_app.dto;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;
import com.example.projectflow_app.domain.Type;

@Data
public class DiagramDataDTO {
    @NotNull
    private String title;

    @NotNull
    private Type type;

    public DiagramDataDTO(String title) {
        this.title = title;
    }
}