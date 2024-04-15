package ru.practicum.events.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.categories.model.Category;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.users.model.User;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String annotation;

    @JoinColumn(name = "cat_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    private long confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    private String description;

    private LocalDateTime eventDate;

    @JoinColumn(name = "initiator")
    @ManyToOne(fetch = FetchType.LAZY)
    private User initiator;

    @JoinColumn(name = "location")
    @ManyToOne(fetch = FetchType.LAZY)
    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private EventState state;

    @Column
    private String title;

    @Column
    private long views;

    @ManyToMany
    @JoinTable(name = "compilation_to_event",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "compilation_id"))
    private List<Compilation> compilations = new ArrayList<>();
}
