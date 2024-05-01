package ru.practicum.comments.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class CommentRequestDto {
    @NotNull
    @NotBlank
    @Size(min = 3, max = 2000)
    String text;
}
