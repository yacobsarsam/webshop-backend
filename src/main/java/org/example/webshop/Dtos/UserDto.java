package org.example.webshop.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.webshop.Model.Role;
import org.example.webshop.Model.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id; // Changed to Long to match the User entity
    private String email;
    private Role role;

    public static UserDto fromEntity(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
    }
}