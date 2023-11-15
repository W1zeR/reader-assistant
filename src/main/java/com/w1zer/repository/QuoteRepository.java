package com.w1zer.repository;

import com.w1zer.entity.Quote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuoteRepository extends CrudRepository<Quote, Long> {
    @Override
    @NonNull
    List<Quote> findAll();

    Optional<Quote> findToadById(Long id);

    List<Quote> findToadsByIdProfile(Long idProfile);
}
