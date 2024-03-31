package ru.practicum.compilations.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.compilations.model.Compilation;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    List<Compilation> getCompilationByPinned(Boolean pinned, Pageable pageable);
}
