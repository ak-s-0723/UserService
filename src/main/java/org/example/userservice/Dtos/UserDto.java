package org.example.userservice.Dtos;

import lombok.Getter;
import lombok.Setter;
import org.example.userservice.Models.Role;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserDto {
    private String email;

    private Set<Role> roles = new HashSet<>();
}
