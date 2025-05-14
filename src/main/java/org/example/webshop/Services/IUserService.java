package org.example.webshop.Services;

import org.example.webshop.Dtos.CategoryDto;
import org.example.webshop.Dtos.UserDto;
import org.example.webshop.Model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {
    void DeleteUser(Long id);
    Page<UserDto> GetUsers(Pageable pageable);

    UserDto UpdateUser(UserDto userDto);
}