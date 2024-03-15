package ru.practicum.users.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Builder
@Data
public class NewUserRequest {
    @Email
    @NotBlank
    @Length(min = 6, max = 254)
    private String email;

    @NotBlank
    @Length(min = 2, max = 250)
    private String name;
}
