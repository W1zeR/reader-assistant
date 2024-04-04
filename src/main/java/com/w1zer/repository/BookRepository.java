package com.w1zer.repository;

import com.w1zer.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
	@Override
    @NonNull
    List<Book> findAll();

    @NonNull
    Optional<Book> findById(@NonNull Long id);
}
