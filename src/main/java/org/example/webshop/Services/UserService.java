package org.example.webshop.Services;

import org.example.webshop.Dtos.UserDto;
import org.example.webshop.Model.User;
import org.example.webshop.Repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public UserDto[] getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserDto::fromEntity).toArray(UserDto[]::new);
    }
    @Override
    public void DeleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
    }
    @Override
    public Page<UserDto> GetUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserDto::fromEntity);
    }

    @Override
    public UserDto UpdateUser(UserDto userDto) {
        User existingUser = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + userDto.getId()));

        // Update the fields of the existing product
        existingUser.setEmail(userDto.getEmail());
        existingUser.setRole(userDto.getRole());

        // Save the updated product
        User updatedUser = userRepository.save(existingUser);

        // Convert the updated product to a DTO and return it
        return UserDto.fromEntity(updatedUser);
    }
}