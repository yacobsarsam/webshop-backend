package org.example.webshop.Controller;

import org.example.webshop.Dtos.CategoryDto;
import org.example.webshop.Dtos.ProductDto;
import org.example.webshop.Dtos.UserDto;
import org.example.webshop.Model.User;
import org.example.webshop.Services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserDto>>getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String ordering)
    {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(ordering != null ? ordering : "id"));
            Page<UserDto> users = userService.GetUsers(pageable);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @RequestBody UserDto userDto) {
        try {
            // Set the product ID to ensure the correct product is updated
            userDto.setId(id);

            // Call the service to update the product
            UserDto updatedProduct = userService.UpdateUser(userDto);

            return ResponseEntity.ok(updatedProduct); // Return the updated product
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build(); // Return 404 if the product is not found
        } catch (Exception e) {
            return ResponseEntity.status(500).build(); // Return 500 for other errors
        }
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> DeleteUser(@PathVariable Long id) {
        try {
            userService.DeleteUser(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}