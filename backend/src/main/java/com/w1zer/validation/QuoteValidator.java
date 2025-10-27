package com.w1zer.validation;

import com.w1zer.entity.Quote;
import com.w1zer.entity.QuoteStatusName;
import com.w1zer.exception.AuthException;
import com.w1zer.exception.QuoteChangeException;
import com.w1zer.repository.QuoteRepository;
import com.w1zer.security.UserPrincipal;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class QuoteValidator {
    public static final String EMPTY_STRING = "";
    public static final String NOT_PRIVATE = "Can't change a quote that is not private";
    public static final String QUOTE_STATUS_MUST_BE_PRIVATE = "Current quote status must be private";
    public static final String QUOTE_STATUS_MUST_BE_PENDING = "Current quote status must be pending";
    public static final String QUOTE_STATUS_MUST_BE_PUBLIC = "Current quote status must be public";

    private final QuoteRepository quoteRepository;

    public QuoteValidator(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    public void validateQuoteOwning(Long quoteId, UserPrincipal userPrincipal) {
        Optional<Quote> optionalQuote = quoteRepository.findById(quoteId);
        if (optionalQuote.isEmpty()) {
            return;
        }
        if (!Objects.equals(optionalQuote.get().getProfile().getId(), userPrincipal.getId())) {
            throw new AuthException(EMPTY_STRING);
        }
    }

    public void validateQuoteIsPublicOrQuoteOwning(Long quoteId, Long profileId) {
        Optional<Quote> optionalQuote = quoteRepository.findById(quoteId);
        if (optionalQuote.isEmpty()) {
            return;
        }
        Quote quote = optionalQuote.get();
        if (quote.getStatus().getName().equals(QuoteStatusName.PUBLIC)) {
            return;
        }
        if (!Objects.equals(quote.getProfile().getId(), profileId)) {
            throw new AuthException(EMPTY_STRING);
        }
    }

    public void validateQuoteChange(Long quoteId) {
        Optional<Quote> optionalQuote = quoteRepository.findById(quoteId);
        if (optionalQuote.isEmpty()) {
            return;
        }
        if (!optionalQuote.get().getStatus().getName().equals(QuoteStatusName.PRIVATE)) {
            throw new QuoteChangeException(NOT_PRIVATE);
        }
    }

    public void validateCurrentQuoteStatusName(QuoteStatusName quoteStatusNameFromEntity,
                                               QuoteStatusName quoteStatusNameFromRequest) {
        if (quoteStatusNameFromEntity != quoteStatusNameFromRequest) {
            throw new QuoteChangeException(getExceptionMessageByQuoteStatusName(quoteStatusNameFromRequest));
        }
    }

    private String getExceptionMessageByQuoteStatusName(QuoteStatusName statusName) {
        if (statusName == QuoteStatusName.PRIVATE) {
            return QUOTE_STATUS_MUST_BE_PRIVATE;
        }
        if (statusName == QuoteStatusName.PENDING) {
            return QUOTE_STATUS_MUST_BE_PENDING;
        }
        return QUOTE_STATUS_MUST_BE_PUBLIC;
    }
}
