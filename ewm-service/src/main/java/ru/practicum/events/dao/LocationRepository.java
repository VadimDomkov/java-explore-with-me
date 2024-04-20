package ru.practicum.events.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.events.model.Location;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findLocationByLatAndLon(double lat, double lon);
}
