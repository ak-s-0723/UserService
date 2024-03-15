package org.example.userservice.Dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ValidateRequestDto {
    private String token;
    private Long id;
}
