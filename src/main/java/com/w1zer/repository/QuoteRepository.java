package com.w1zer.repository;

import com.w1zer.entity.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {
    @Override
    @NonNull
    List<Quote> findAll();

    @NonNull
    Optional<Quote> findById(@NonNull Long id);
}
