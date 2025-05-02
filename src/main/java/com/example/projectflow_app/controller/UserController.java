package com.example.projectflow_app.controller;

import com.example.projectflow_app.domain.User;
import com.example.projectflow_app.dto.UserDTO;
import com.example.projectflow_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Objects;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping//если будет переход из строки 13 (по юзерам), то будет срабатывать данный метод
    public String userList(Model model) {
        model.addAttribute("users", userService.getAll());
        return "userList";
    }

    @GetMapping("new")
    public String newUser(Model model) {
        model.addAttribute("user", new UserDTO());
        return "user";
    }

    @PostMapping("new")
    public String saveUser(UserDTO userDTO, Model model) {
        if (userService.save(userDTO)) {
            return "redirect:/users";
        }
        else {
            model.addAttribute("user", userDTO);
            return "user";
        }
    }

    @GetMapping("/profile")
    public String profileUser(Model model, Principal principal) { //principal - это авторизованный юзер
        if (principal == null) {                                  //если авторизованного юзера нет
            throw new RuntimeException("You are is not authorize"); //ошибка - вы не авторизованы
        }
        User user = userService.findByUsername(principal.getName());

        UserDTO dto = UserDTO.builder()                           //если авторизованы - строим ДТОшку
                .username(user.getName())
                .email(user.getEmail())
                .build();
        model.addAttribute("user", dto);
        return "profile";                                        //возвращаемся на страницу profile
    }

    @PostMapping("/profile")
    public String updateProfileUser(UserDTO dto, Model model, Principal principal) { //на страницу мы закидываем ДТОшку, соответственного с сайта ДТОшка и будет приходить
        if (principal == null || !Objects.equals(principal.getName(), dto.getUsername())) { //если имя авторизованного юзера не совпадает с ДТОшкой, то мы не авторизованы
            throw new RuntimeException("You are is not authorize");
        }
        if (dto.getPassword() != null
                && !dto.getPassword().isEmpty()
                && !Objects.equals(dto.getPassword(), dto.getMatchingPassword())) {
            model.addAttribute("user", dto);         //если все поля корректно заполнены, возвращаем ДТОшку
            //нужно добавить какое-то сообщение, но сделаем это в другой раз
            return "profile";
        }
        userService.updateProfile(dto);
        return "redirect:/users/profile";
    }

}
