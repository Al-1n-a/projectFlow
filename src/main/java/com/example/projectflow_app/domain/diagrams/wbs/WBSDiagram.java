package com.example.projectflow_app.domain.diagrams.wbs;

import com.example.projectflow_app.domain.diagrams.common.Diagram;

public class WBSDiagram extends Diagram {
    @Override
    public String getDefaultConfig() {
        //надо изменить потом
        return """
        {
            "tasks": [],
            "startDate": "2023-01-01",
            "endDate": "2023-12-31"
        }
        """;
    }
}
