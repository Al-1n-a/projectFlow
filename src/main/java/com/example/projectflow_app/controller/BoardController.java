package com.example.projectflow_app.controller;

import com.example.projectflow_app.domain.Board;
import com.example.projectflow_app.domain.Type;
import com.example.projectflow_app.domain.User;
import com.example.projectflow_app.dto.DiagramTypeDTO;
import com.example.projectflow_app.service.BoardService;
import com.example.projectflow_app.service.DiagramService;
import com.example.projectflow_app.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;
    private final DiagramService diagramService;
    private final UserService userService;

    @Autowired
    public BoardController(BoardService boardService, DiagramService diagramService, UserService userService) {
        this.boardService = boardService;
        this.diagramService = diagramService;
        this.userService = userService;
    }

    @GetMapping
    public String getUserBoards(Principal principal, Model model,
                                HttpServletRequest request) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("boards", user.getBoards());

        // Добавляем CSRF токен
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("_csrf", csrfToken);

        return "boards/boardsList";
    }

    @GetMapping("/") //для отображения списка досок пользователя в главном меню
    public String home(Principal principal, Model model) {
        if (principal != null) {
            User user = userService.findByUsername(principal.getName());
            model.addAttribute("boards", user.getBoards());
        } else {
            model.addAttribute("boards", Collections.emptyList());
        }
        return "menu";
    }

    @GetMapping("/create")
    public String showCreateBoardForm(Model model) {
        model.addAttribute("board", new Board());
        return "boards/createBoard";
    }

    @PostMapping("/create")
    public String createBoard(@ModelAttribute Board board, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Board createdBoard = boardService.createBoard(user, board.getTitle(), board.getDescription(), Collections.emptyList());
        return "redirect:/boards/" + createdBoard.getId();
    }

    @GetMapping("/{boardId}")
    public String viewBoard(@PathVariable Long boardId, Model model, Principal principal) {
        if (principal == null || !userService.existsByName(principal.getName())) {
            return "redirect:/login";
        }

        User user = userService.findByUsername(principal.getName());

        if (!boardService.isBoardOwnedByUser(boardId, user.getId())) {
            return "redirect:/boards";
        }

        Board board = boardService.findBoardWithDiagrams(boardId);
        if (board == null || board.getId() == null) {
            return "redirect:/boards";
        }

        model.addAttribute("board", board);
        model.addAttribute("diagramTypes", Arrays.stream(Type.values())
                .map(type -> new DiagramTypeDTO(type.name(), type.getDisplayName()))
                .collect(Collectors.toList()));

        return "boards/boardView1";
    }

    @DeleteMapping("/{boardId}")
    @ResponseBody
    public ResponseEntity<Void> deleteBoard(
            @PathVariable Long boardId,
            Principal principal) {
        // Проверяем, что доска принадлежит текущему пользователю
        User user = userService.findByUsername(principal.getName());
        if (!boardService.isBoardOwnedByUser(boardId, user.getId())) {
            return ResponseEntity.status(403).build();
        }

        boardService.deleteBoard(boardId);
        return ResponseEntity.ok().build();
    }
}
