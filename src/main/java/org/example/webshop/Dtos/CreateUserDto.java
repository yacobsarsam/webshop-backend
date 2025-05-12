package org.example.webshop.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.webshop.Model.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {
    private String email;
    private String password;
    private Role role;
}
