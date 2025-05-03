package com.example.projectflow_app.controller;

import com.example.projectflow_app.dao.DiagramRepository;
import com.example.projectflow_app.domain.Type;
import com.example.projectflow_app.domain.User;
import com.example.projectflow_app.dto.DiagramDTO;
import com.example.projectflow_app.dto.DiagramDataDTO;
import com.example.projectflow_app.dto.DiagramUpdateDTO;
import com.example.projectflow_app.dto.PositionDTO;
import com.example.projectflow_app.service.BoardService;
import com.example.projectflow_app.service.DiagramService;
import com.example.projectflow_app.service.UserService;
import com.example.projectflow_app.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/boards/{boardId}/diagrams")
public class DiagramController {

    private final DiagramService diagramService;
    private final UserService userService;
    private final BoardService boardService;

    @Autowired
    public DiagramController(DiagramService diagramService, UserService userService, BoardService boardService) {
        this.diagramService = diagramService;
        this.userService = userService;
        this.boardService = boardService;
    }

//    @GetMapping
//    public String showDiagrams(Principal principal, Model model) {
//        User user = userService.findByName(principal.getName());
//        List<DiagramDTO> diagramDTOList = diagramService.getAllUsersDiagrams(user.getName());
//        model.addAttribute("diagrams", diagramDTOList);
//        return "diagrams"; //будем передавать во вьюху diagrams(27) диаграммы (26)
//    }

    @PostMapping
    public ResponseEntity<DiagramDTO> createDiagram(
            @PathVariable Long boardId,
            @RequestBody Map<String, String> request,
            Principal principal) {
        try {
            String diagramType = request.get("type");
            DiagramDTO diagram = diagramService.createDiagram(
                    boardId,
                    diagramType,
                    principal.getName()
            );
            return ResponseEntity.ok(diagram);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{diagramId}/config")
    public DiagramDTO updateConfig(
            @PathVariable Long diagramId,
            @RequestBody String configJson
    ) {
        return diagramService.updateDiagramConfig(diagramId, configJson);
    }


    @PostMapping("{diagramId}/position")
    @ResponseBody
    public ResponseEntity<Void> updateDiagramPosition(
            @PathVariable Long boardId,
            @PathVariable Long diagramId,
            @RequestBody PositionDTO position) {

        diagramService.updateDiagramPosition(diagramId, position);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{diagramId}")
    public ResponseEntity<DiagramDTO> updateDiagram(
            @PathVariable Long diagramId,
            @RequestBody DiagramUpdateDTO updateDTO,
            Principal principal) {

        if (updateDTO == null) {
            return ResponseEntity.badRequest().build();
        }
        DiagramDTO updated = diagramService.updateDiagram(diagramId, updateDTO, principal.getName());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{diagramId}")
    public ResponseEntity<Void> deleteDiagram(
            @PathVariable Long boardId,
            @PathVariable Long diagramId,
            Principal principal) {
        try {
            System.out.println("Attempting to delete diagram " + diagramId + " from board " + boardId);
            diagramService.deleteDiagram(diagramId, boardId, principal.getName());
            System.out.println("Diagram deleted successfully");
            return ResponseEntity.ok().build();
        } catch (SecurityException e) {
            System.err.println("Security exception: " + e.getMessage());
            return ResponseEntity.status(403).build();
        } catch (RuntimeException e) {
            System.err.println("Runtime exception: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
