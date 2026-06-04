package com.ecommerce.user.dto;

import com.ecommerce.user.model.UserRole;
import lombok.Data;

@Data
public class UserResponseDTO {
    private Long userId;
    private UserRole role;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private AddressDTO address;
}
