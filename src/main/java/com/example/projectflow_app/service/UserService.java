package com.example.projectflow_app.service;

import com.example.projectflow_app.domain.User;
import com.example.projectflow_app.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService { //security

    boolean save(UserDTO userDTO); //для регистрации новых пользователей

    void updateUser(User user); // для сохранения существующих сущностей

    List<UserDTO> getAll();

    User findByUsername(String name);

    void updateProfile(UserDTO dto);

    // Новый метод с JOIN FETCH
    public User findWithBoardsAndDiagramsByUsername(String username);

    public boolean existsByName(String name);
}
