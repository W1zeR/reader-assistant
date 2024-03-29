package com.w1zer.controller;

import com.w1zer.payload.QuoteRequest;
import com.w1zer.service.QuoteService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/quotes")
public class QuoteController {
    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

//    @GetMapping("/quotes")
//    public List<QuoteResponse> getAll() {
//        return quoteService.getAll();
//    }
//
//    @GetMapping("/quotes/{id}")
//    public QuoteResponse getById(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
//        return quoteService.getById(id);
//    }
//
//    @GetMapping("/profiles/{id}/quotes")
//    public List<QuoteResponse> getByIdProfile(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
//        return quoteService.getByIdProfile(id);
//    }
//
//    @PostMapping("/quotes")
//    public QuoteResponse insert(@Valid @RequestBody QuoteRequest quoteRequest) {
//        return quoteService.insert(quoteRequest);
//    }
//
//    @DeleteMapping("/quotes/{id}")
//    public void delete(@PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id) {
//        quoteService.delete(id);
//    }
//
//    @PutMapping("/quotes/{id}")
//    public QuoteResponse update(
//            @PathVariable @Positive(message = ID_POSITIVE_MESSAGE) Long id,
//            @Valid @RequestBody QuoteRequest quoteRequest) {
//        return quoteService.update(id, quoteRequest);
//    }

    @PutMapping("/markAsPending")
    public void markAsPending(@Valid @RequestBody QuoteRequest quoteRequest) {
        quoteService.markAsPending(quoteRequest);
    }

    @PutMapping("/markAsPublic")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public void markAsPublic(@Valid @RequestBody QuoteRequest quoteRequest) {
        quoteService.markAsPublic(quoteRequest);
    }
}
