package com.w1zer.repository;

import com.w1zer.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    @Override
    @NonNull
    List<Tag> findAll();

    @NonNull
    Optional<Tag> findById(@NonNull Long id);

    Optional<Tag> findByName(String name);

    boolean existsByName(String name);
}
