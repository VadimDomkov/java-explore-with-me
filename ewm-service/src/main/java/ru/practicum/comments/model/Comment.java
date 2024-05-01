package ru.practicum.comments.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.events.model.Event;
import ru.practicum.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @JoinColumn(name = "event_id")
    @ManyToOne
    Event event;
    @JoinColumn(name = "user_id")
    @ManyToOne
    User author;
    String text;
    @Enumerated(EnumType.STRING)
    Evaluation evaluation;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime published;
}
