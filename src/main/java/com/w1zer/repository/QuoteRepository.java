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
    String INTERESTING_TAGS_QUERY_START = """
            SELECT q.id, q.content, q.change_date,
                (SELECT COUNT(lq.id_profile)
                FROM liked_quotes lq WHERE lq.id_quote = q.id) likesCount,
            q.book_id, q.profile_id, q.status_id
            FROM quote q
            LEFT JOIN quotes_tags qt ON qt.id_quote = q.id
            LEFT JOIN
                (SELECT qt.id_tag tag, COUNT(lq.id_quote) likes
                FROM quotes_tags qt
                LEFT JOIN liked_quotes lq ON lq.id_quote = qt.id_quote
                LEFT JOIN profile p ON p.id = lq.id_profile AND p.id = ?1
                GROUP BY qt.id_tag) tl ON tl.tag = qt.id_tag
            """;

    String INTERESTING_TAGS_QUERY_END = """
            GROUP BY q.id, q.content, q.change_date, q.likes_count, q.book_id, q.profile_id, q.status_id
            ORDER BY MAX(tl.likes) DESC, q.change_date DESC""";

    String INTERESTING_TAGS_PENDING_QUERY_CENTER = """
            WHERE q.status_id = 2
            """;

    String INTERESTING_TAGS_PUBLIC_QUERY_CENTER = """
            WHERE q.status_id = 3
            """;

    String INTERESTING_TAGS_PRIVATE_QUERY_CENTER = """
            WHERE q.status_id = 1 AND q.profile_id = ?1
            """;

    // Public or pending quotes
    Page<Quote> findAllByStatusNameIs(QuoteStatusName statusName, Pageable p);

    // Private quotes
    @SuppressWarnings("SpringElInspection")
    @Query("select q from Quote q where q.profile.id = ?#{ principal?.id }")
    Page<Quote> findAllByStatusIs(QuoteStatus status, Pageable p);

    // Sort by interesting tags for public quotes
    @Query(value = INTERESTING_TAGS_QUERY_START +
            INTERESTING_TAGS_PUBLIC_QUERY_CENTER +
            INTERESTING_TAGS_QUERY_END,
            nativeQuery = true)
    Page<Quote> findPublicQuotesSortByInterestingTags(Long principalId, Pageable p);

    // Sort by interesting tags for private quotes
    @Query(value = INTERESTING_TAGS_QUERY_START +
            INTERESTING_TAGS_PENDING_QUERY_CENTER +
            INTERESTING_TAGS_QUERY_END,
            nativeQuery = true)
    Page<Quote> findPendingQuotesSortByInterestingTags(Long principalId, Pageable p);

    // Sort by interesting tags for private quotes
    @Query(value = INTERESTING_TAGS_QUERY_START +
            INTERESTING_TAGS_PRIVATE_QUERY_CENTER +
            INTERESTING_TAGS_QUERY_END,
            nativeQuery = true)
    Page<Quote> findPrivateQuotesSortByInterestingTags(Long principalId, Pageable p);

    @NonNull
    Optional<Quote> findById(@NonNull Long id);
}
