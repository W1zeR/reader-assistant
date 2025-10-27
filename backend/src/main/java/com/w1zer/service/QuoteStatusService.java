package com.w1zer.service;

import com.w1zer.entity.QuoteStatus;
import com.w1zer.entity.QuoteStatusName;
import com.w1zer.exception.NotFoundException;
import com.w1zer.repository.QuoteStatusRepository;
import org.springframework.stereotype.Service;

@Service
public class QuoteStatusService {
    private static final String QUOTE_STATUS_WITH_NAME_NOT_FOUND = "Quote status with name '%s' not found";

    private final QuoteStatusRepository quoteStatusRepository;

    public QuoteStatusService(QuoteStatusRepository quoteStatusRepository) {
        this.quoteStatusRepository = quoteStatusRepository;
    }

    public QuoteStatus findByName(QuoteStatusName name) {
        return quoteStatusRepository.findByName(name).orElseThrow(
                () -> new NotFoundException(QUOTE_STATUS_WITH_NAME_NOT_FOUND.formatted(name))
        );
    }
}
