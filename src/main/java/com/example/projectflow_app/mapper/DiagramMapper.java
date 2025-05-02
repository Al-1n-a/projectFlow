package com.example.projectflow_app.mapper;

import com.example.projectflow_app.domain.Type;
import com.example.projectflow_app.domain.diagrams.common.Diagram;
import com.example.projectflow_app.domain.diagrams.dfd.DFDDiagram;
import com.example.projectflow_app.domain.diagrams.gantt.GantDiagram;
import com.example.projectflow_app.dto.DiagramDTO;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DiagramMapper {

    //@SubclassMapping(source = DiagramDTO.class, target = Diagram.class)
    default Diagram toDiagram(DiagramDTO dto) {
        if (dto == null) return null;

        Type type;
        try {
            type = Type.valueOf(dto.getType().toUpperCase());
        } catch (IllegalArgumentException e) {
            if (dto.getType() == null) {
                throw new IllegalArgumentException("Diagram type cannot be null");
            } else {
                throw new IllegalArgumentException("Unknown diagram type: " + dto.getType());
            }
        }

        return switch (type) {
            case GANTT -> toGantDiagram(dto);
            case DFD -> toDfdDiagram(dto);
//            case WBS -> toWbsDiagram(dto);
//            case SAM -> toSamDiagram(dto);
//            case TIMELINE -> toTimelineDiagram(dto);
//            case CFD -> toCfdDiagram(dto);
//            case AGENDA -> toAgendaDiagram(dto);
//            case ISHIKAWA -> toIsikawaDiagram(dto);
//            case RT -> toRtDiagram(dto);
//            case STRATEGYMAP -> toSrtategymapDiagram(dto);
//            case AGILE -> toAgileDiagram(dto);
            default -> throw new IllegalArgumentException("Unknown type: " + dto.getType());
        };
    }
    //@Mapping(target = "tasks", ignore = true)
    GantDiagram toGantDiagram(DiagramDTO dto);

    //@Mapping(target = "elements", ignore = true)
    DFDDiagram toDfdDiagram(DiagramDTO dto);

    // WBSDiagram toWbsDiagram(DiagramDTO dto);
    // SAMDiagram toSamDiagram(DiagramDTO dto);

    @Named("diagramToDto")
    @Mapping(target = "type", expression = "java(getDiagramType(diagram))")
    DiagramDTO toDTO(Diagram diagram);

    default String getDiagramType(Diagram diagram) {
        if (diagram instanceof GantDiagram) return "GANTT";
        if (diagram instanceof DFDDiagram) return "DFD";
        throw new IllegalArgumentException("Unknown diagram type");
    }

    default List<Diagram> toDiagramList(List<DiagramDTO> diagramDTOS) {
        if (diagramDTOS == null) return null;
        return diagramDTOS.stream()
                .map(this::toDiagram)
                .collect(Collectors.toList());
    }

    @IterableMapping(qualifiedByName = "diagramToDto")
    List<DiagramDTO> fromDiagramList(List<Diagram> diagrams);

}