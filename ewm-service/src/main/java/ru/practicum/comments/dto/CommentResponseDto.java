package ru.practicum.comments.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.comments.model.Evaluation;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class CommentResponseDto {
    String eventTitle;
    String userName;
    String text;
    Evaluation evaluation;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime published;
}
