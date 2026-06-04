package com.ecommerce.user.service;

import com.ecommerce.user.dto.UserRequestDTO;
import com.ecommerce.user.dto.UserResponseDTO;
import com.ecommerce.user.model.Users;

import java.util.List;

public interface I_UserService {
    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(Long id);

    UserResponseDTO createUser(UserRequestDTO user);

    UserResponseDTO updateUser(Long id, UserRequestDTO user);

    void deleteUser(Long id);

}
