package com.w1zer.repository;

import com.w1zer.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Override
    @NonNull
    List<Author> findAll();

    @NonNull
    Optional<Author> findById(@NonNull Long id);
}
