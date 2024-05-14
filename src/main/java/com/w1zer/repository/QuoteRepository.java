package com.w1zer.repository;

import com.w1zer.entity.Quote;
import com.w1zer.entity.QuoteStatus;
import com.w1zer.entity.QuoteStatusName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {
    // Public or pending quotes
    Page<Quote> findAllByStatusNameIs(QuoteStatusName statusName, Pageable p);

    // Private quotes
    @SuppressWarnings("SpringElInspection")
    @Query("select q from Quote q where q.profile.id = ?#{ principal?.id }")
    Page<Quote> findAllByStatusIs(QuoteStatus status, Pageable p);

    // Sort by interesting tags for public or pending quotes
    @Query("select q from Quote q ")
    Page<Quote> findQuotesByStatusNameIs(QuoteStatusName statusName, Pageable p);

    @NonNull
    Optional<Quote> findById(@NonNull Long id);
}
