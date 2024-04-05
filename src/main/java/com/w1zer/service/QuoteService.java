package com.w1zer.service;

import com.w1zer.entity.Quote;
import com.w1zer.entity.QuoteStatus;
import com.w1zer.entity.QuoteStatusName;
import com.w1zer.exception.NotFoundException;
import com.w1zer.payload.QuoteRequest;
import com.w1zer.payload.UpdateQuoteRequest;
import com.w1zer.repository.QuoteRepository;
import org.springframework.stereotype.Service;

@Service
public class QuoteService {
    private static final String QUOTE_WITH_ID_NOT_FOUND = "Quote with id '%d' not found";

    private final QuoteRepository quoteRepository;
    private final QuoteStatusService quoteStatusService;
//    private final QuoteMapper quoteMapper;

    public QuoteService(QuoteRepository quoteRepository,
//            , QuoteMapper quoteMapper
                        QuoteStatusService quoteStatusService) {
        this.quoteRepository = quoteRepository;
//        this.quoteMapper = quoteMapper;
        this.quoteStatusService = quoteStatusService;
    }

//    public List<QuoteResponse> getAll() {
//        return quoteMapper.mapToQuoteResponseList(quoteRepository.findAll());
//    }
//
//    public QuoteResponse getById(Long id) {
//        Quote quote = quoteRepository.findQuoteById(id).orElseThrow(
//                () -> new NotFoundException(QUOTE_WITH_ID_NOT_FOUND.formatted(id))
//        );
//        return quoteMapper.mapToQuoteResponse(quote);
//    }
//
//    public List<QuoteResponse> getByIdProfile(Long idProfile) {
//        return quoteMapper.mapToQuoteResponseList(quoteRepository.findQuotesByProfileId(idProfile));
//    }
//
//    public QuoteResponse insert(QuoteRequest quoteRequest) {
//        Quote quote = quoteRepository.save(quoteMapper.mapToQuote(quoteRequest));
//        return quoteMapper.mapToQuoteResponse(quote);
//    }
//
//    public void delete(Long id) {
//        if (!quoteRepository.existsById(id)) {
//            throw new NotFoundException(QUOTE_WITH_ID_NOT_FOUND.formatted(id));
//        }
//        quoteRepository.deleteById(id);
//    }
//
//    public QuoteResponse update(Long id, QuoteRequest quoteRequest) {
//        if (!quoteRepository.existsById(id)) {
//            throw new NotFoundException(QUOTE_WITH_ID_NOT_FOUND.formatted(id));
//        }
//        Quote quote = quoteRepository.save(quoteMapper.mapToQuote(id, quoteRequest));
//        return quoteMapper.mapToQuoteResponse(quote);
//    }

    public void markAsPending(QuoteRequest quoteRequest) {
        Quote quote = findById(quoteRequest.id());
        QuoteStatus quoteStatus = quote.getQuoteStatus();
        if (quoteStatus.getName() != QuoteStatusName.PRIVATE) {
            throw new RuntimeException("Quote status must be private");
        }
        QuoteStatus pending = quoteStatusService.findByName(QuoteStatusName.PENDING);
        quote.setQuoteStatus(pending);
        quoteRepository.save(quote);
    }

    public void markAsPublic(QuoteRequest quoteRequest) {
        Quote quote = findById(quoteRequest.id());
        QuoteStatus quoteStatus = quote.getQuoteStatus();
        if (quoteStatus.getName() != QuoteStatusName.PENDING) {
            throw new RuntimeException("Quote status must be pending");
        }
        QuoteStatus pub = quoteStatusService.findByName(QuoteStatusName.PUBLIC);
        quote.setQuoteStatus(pub);
        quoteRepository.save(quote);
    }

    public Quote findById(Long id) {
        return quoteRepository.findById(id).orElseThrow(
                () -> new NotFoundException(QUOTE_WITH_ID_NOT_FOUND.formatted(id))
        );
    }
	
	public void delete(Long id) {
        quoteRepository.deleteById(id);
    }
	
	public Quote update(UpdateQuoteRequest updateQuoteRequest, Long id) {
        Quote quote = quoteRepository.findById(id).orElseThrow(
                () -> new NotFoundException(QUOTE_WITH_ID_NOT_FOUND)
        );
        if (updateQuoteRequest.content() != null) {
            quote.setContent(updateQuoteRequest.content());
        }
        return quoteRepository.save(quote);
    }
}
