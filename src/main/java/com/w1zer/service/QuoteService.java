package com.w1zer.service;

import com.w1zer.entity.Quote;
import com.w1zer.exception.NotFoundException;
import com.w1zer.mapping.QuoteMapper;
import com.w1zer.payload.QuoteRequest;
import com.w1zer.payload.QuoteResponse;
import com.w1zer.repository.QuoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuoteService {
    private static final String QUOTE_WITH_ID_NOT_FOUND = "Quote with id '%d' not found";

    private final QuoteRepository quoteRepository;
    private final QuoteMapper quoteMapper;

    public QuoteService(QuoteRepository quoteRepository, QuoteMapper quoteMapper) {
        this.quoteRepository = quoteRepository;
        this.quoteMapper = quoteMapper;
    }

    public List<QuoteResponse> getAll() {
        return quoteMapper.mapToQuoteResponseList(quoteRepository.findAll());
    }

    public QuoteResponse getById(Long id) {
        Quote quote = quoteRepository.findQuoteById(id).orElseThrow(
                () -> new NotFoundException(QUOTE_WITH_ID_NOT_FOUND.formatted(id))
        );
        return quoteMapper.mapToQuoteResponse(quote);
    }

    public List<QuoteResponse> getByIdProfile(Long idProfile) {
        return quoteMapper.mapToQuoteResponseList(quoteRepository.findQuotesByProfileId(idProfile));
    }

    public QuoteResponse insert(QuoteRequest quoteRequest) {
        Quote quote = quoteRepository.save(quoteMapper.mapToQuote(quoteRequest));
        return quoteMapper.mapToQuoteResponse(quote);
    }

    public void delete(Long id) {
        if (!quoteRepository.existsById(id)) {
            throw new NotFoundException(QUOTE_WITH_ID_NOT_FOUND.formatted(id));
        }
        quoteRepository.deleteById(id);
    }

    public QuoteResponse update(Long id, QuoteRequest quoteRequest) {
        if (!quoteRepository.existsById(id)) {
            throw new NotFoundException(QUOTE_WITH_ID_NOT_FOUND.formatted(id));
        }
        Quote quote = quoteRepository.save(quoteMapper.mapToQuote(id, quoteRequest));
        return quoteMapper.mapToQuoteResponse(quote);
    }
}
