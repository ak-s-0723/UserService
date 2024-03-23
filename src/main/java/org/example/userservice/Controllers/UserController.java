package org.example.userservice.Controllers;

import org.example.userservice.Dtos.UserDto;
import org.example.userservice.Models.User;
import org.example.userservice.Services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/{id}")
    public UserDto GetUserDetails(@PathVariable Long id) {
        System.out.println(id);
        User user =  userService.getUserDetails(id);
        return getUserDto(user);
    }

    private UserDto getUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}
