package com.w1zer.repository;

import com.w1zer.entity.QuoteStatus;
import com.w1zer.entity.QuoteStatusName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuoteStatusRepository extends JpaRepository<QuoteStatus, Long> {
    Optional<QuoteStatus> findByName(QuoteStatusName name);
}
