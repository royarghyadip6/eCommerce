package com.ecommerce.user.service.impl;

import com.ecommerce.user.dto.AddressDTO;
import com.ecommerce.user.dto.UserRequestDTO;
import com.ecommerce.user.dto.UserResponseDTO;
import com.ecommerce.user.model.Address;
import com.ecommerce.user.model.Users;
import com.ecommerce.user.repository.I_UsersReposiroty;
import com.ecommerce.user.service.I_UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements I_UserService {

    private final I_UsersReposiroty userRepository;

    /**
     * Get all users from the database and convert them to UserResponseDTO objects.
     * If there are no users, throw a RuntimeException with a message indicating that no data is present.
     * @return a list of UserResponseDTO objects representing all users in the database
     */
    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<Users> users =  userRepository.findAll();
        List<UserResponseDTO> userResponseDTOList = users.stream().map(this::mapToUserResponse).toList();
        if (userResponseDTOList != null) {
            return userResponseDTOList;
        } else {
            throw new RuntimeException("No data present. Please insert some data.");
        }
    }

    /**
     * Get a user by their ID from the database. If the user is not found, throw a RuntimeException with a message indicating that the data was not found.
     * @param id the ID of the user to retrieve
     * @return a UserResponseDTO object representing the retrieved user
     */
    @Override
    public UserResponseDTO getUserById(Long id) {
        Users user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Data not found with id:"+id));
        return mapToUserResponse(user);
    }

    /**
     * Create a new user in the database.
     * @param userRequestDTO the DTO containing the user information
     * @return a UserResponseDTO object representing the created user
     */
    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        Users user = userRequestDTOToUserMapping(userRequestDTO);
        Users savedUser = userRepository.save(user);
        System.out.println(savedUser);
        return mapToUserResponse(savedUser);
    }

    /**
     * Update an existing user in the database.
     * @param id the ID of the user to update
     * @param userRequestDTO the DTO containing the updated user information
     * @return a UserResponseDTO object representing the updated user
     */
    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        if (userRepository.existsById(id)) {
            Users user = userRequestDTOToUserMapping(userRequestDTO);
            Users savedUser = userRepository.save(user);
            return mapToUserResponse(savedUser);
        } else {
            throw new RuntimeException("Data not found with id:" + id);
        }
    }

    /**
     * Delete a user from the database by their ID. If the user is not found, throw a RuntimeException with a message indicating that the data was not found.
     * @param id the ID of the user to delete
     */
    @Override
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("Data not found with id:" + id);
        }
    }

    /**
     * Convert a UserRequestDTO object to a Users entity object.
     * @param userRequestDTO the DTO containing the user information
     * @return a Users entity object representing the user information from the DTO
     */
    private Users userRequestDTOToUserMapping(UserRequestDTO userRequestDTO) {
        Users users = new Users();
        users.setFirstName(userRequestDTO.getFirstName());
        users.setLastName(userRequestDTO.getLastName());
        users.setEmail(userRequestDTO.getEmail());
        users.setPhoneNumber(userRequestDTO.getPhoneNumber());
        AddressDTO addressDTO = userRequestDTO.getAddress();
        if (addressDTO != null) {
            Address address = new Address();
            address.setStreet(addressDTO.getStreet());
            address.setCity(addressDTO.getCity());
            address.setState(addressDTO.getState());
            address.setCountry(addressDTO.getCountry());
            address.setZipCode(addressDTO.getZipCode());
            users.setAddress(address);
        }
        return users;
    }

    /**
     * Convert a Users entity object to a UserResponseDTO object.
     * @param users the Users entity object representing the user information
     * @return a UserResponseDTO object representing the user information from the Users entity
     */
    private UserResponseDTO mapToUserResponse(Users users) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUserId(users.getUserId());
        userResponseDTO.setFirstName(users.getFirstName());
        userResponseDTO.setLastName(users.getLastName());
        userResponseDTO.setEmail(users.getEmail());
        userResponseDTO.setPhoneNumber(users.getPhoneNumber());
        userResponseDTO.setRole(users.getRole());

        if (userResponseDTO.getAddress() == null) {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setStreet(users.getAddress().getStreet());
            addressDTO.setCity(users.getAddress().getCity());
            addressDTO.setCountry(users.getAddress().getCountry());
            addressDTO.setState(users.getAddress().getState());
            addressDTO.setZipCode(users.getAddress().getZipCode());
            userResponseDTO.setAddress(addressDTO);
        }
        return userResponseDTO;
    }
}
