package com.brunoams.accesscontrol.web.dto;

import com.brunoams.accesscontrol.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class RegisterRequestDto {
    @Email(message = "e-mail invalido", regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
    @NotBlank
    private String username;
    @NotBlank
    @Size(min = 6, max = 12)
    private String password;
    @NotNull
    private Role role;
}
