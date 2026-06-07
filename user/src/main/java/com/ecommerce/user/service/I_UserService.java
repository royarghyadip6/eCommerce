package com.ecommerce.user.service;

import com.ecommerce.user.dto.UserRequestDTO;
import com.ecommerce.user.dto.UserResponseDTO;

import java.util.List;

public interface I_UserService {
    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(String id);

    UserResponseDTO createUser(UserRequestDTO user);

    UserResponseDTO updateUser(String id, UserRequestDTO user);

    void deleteUser(String id);

}
