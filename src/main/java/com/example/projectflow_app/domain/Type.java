package com.example.projectflow_app.domain;

public enum Type {
    GANTT("Диаграмма Ганта"),
    DFD("Диаграмма потока данных"),
    WBS("Структура распределения работ"),
    SAM("Матрица анализа заинтересованных сторон"),
    TIMELINE("Временная шкала проекта"),
    CFD("Накопительная диаграмма потоков"),
    AGENDA("Шаблон повестки дня"),
    ISHIKAWA("Шаблон «рыбий скелет» (диаграмма Исикавы)"),
    RT("Шаблон исследования"),
    STRATEGYMAP("Шаблон стратегической карты"),
    AGILE("Шаблон Agile-доска");

    private final String displayName;

    Type(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
