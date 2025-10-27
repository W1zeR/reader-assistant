package com.w1zer.repository;

import com.w1zer.entity.Quote;
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
            SELECT q.id, q.content, q.change_date, COUNT(DISTINCT lq.id_quote) likesCount, q.book_id, q.profile_id,
            q.status_id
            FROM quote q
            LEFT JOIN liked_quotes lq ON lq.id_quote = q.id
            LEFT JOIN quotes_tags qt ON qt.id_quote = q.id
            LEFT JOIN
                (SELECT qt.id_tag tag, COUNT(lq.id_quote) likes
                FROM quotes_tags qt
                LEFT JOIN liked_quotes lq ON lq.id_quote = qt.id_quote
                LEFT JOIN profile p ON p.id = lq.id_profile AND p.id = ?1
                GROUP BY qt.id_tag) tl ON tl.tag = qt.id_tag
            """;

    String INTERESTING_TAGS_KEYWORD_CENTER_START = """
            LEFT JOIN tag t ON t.id = qt.id_tag
            LEFT JOIN profile p ON q.profile_id = p.id
            LEFT JOIN book b ON q.book_id = b.id
            LEFT JOIN authors_books ab ON ab.id_book = b.id
            LEFT JOIN author a ON ab.id_author = a.id
            """;

    String INTERESTING_TAGS_KEYWORD_CENTER_END = """
            AND (UPPER(q.content) LIKE CONCAT('%', UPPER(?2), '%') OR
            UPPER(b.title) LIKE CONCAT('%', UPPER(?2), '%') OR
            UPPER(p.login) LIKE CONCAT('%', UPPER(?2), '%') OR
            UPPER(t.name) LIKE CONCAT('%', UPPER(?2), '%') OR
            UPPER(a.surname) LIKE CONCAT('%', UPPER(?2), '%') OR
            UPPER(a.name) LIKE CONCAT('%', UPPER(?2), '%') OR
            UPPER(a.patronymic) LIKE CONCAT('%', UPPER(?2), '%'))
            """;

    String INTERESTING_TAGS_QUERY_END = """
            GROUP BY q.id, q.content, q.change_date, q.book_id, q.profile_id, q.status_id
            ORDER BY MAX(tl.likes) DESC, q.change_date DESC""";

    String INTERESTING_TAGS_PENDING_QUERY_CENTER = """
            WHERE q.status_id = 2
            """;

    String INTERESTING_TAGS_PUBLIC_QUERY_CENTER = """
            WHERE q.status_id = 3
            """;

    // "AND q.status_id = 1" for only quotes with private status
    String INTERESTING_TAGS_PRIVATE_QUERY_CENTER = """
            WHERE q.profile_id = ?1
            """;

    String FIND_BY_STATUS_NAME_AND_KEYWORD = """
            select q from Quote q
            left join q.book b
            left join b.authors a
            left join q.tags t
            where q.status.name = ?1 and
            (upper(q.content) like concat('%', upper(?2), '%') or
            upper(b.title) like concat('%', upper(?2), '%') or
            upper(q.profile.login) like concat('%', upper(?2), '%') or
            upper(t.name) like concat('%', upper(?2), '%') or
            upper(a.surname) like concat('%', upper(?2), '%') or
            upper(a.name) like concat('%', upper(?2), '%') or
            upper(a.patronymic) like concat('%', upper(?2), '%'))""";

    String PRIVATE_QUOTES_BY_KEYWORD_CONDITION = " and q.profile.id = ?#{ principal?.id }";

    String PRIVATE_QUOTES_QUERY = "select q from Quote q where q.profile.id = ?#{ principal?.id }";

    // Find all public or pending quotes
    Page<Quote> findAllByStatusNameIs(QuoteStatusName statusName, Pageable p);

    // Find public or pending quotes by keyword
    @Query(FIND_BY_STATUS_NAME_AND_KEYWORD)
    Page<Quote> findPublicOrPendingByStatusNameAndKeyword(QuoteStatusName statusName, String keyword, Pageable p);

    // Find all private quotes
    @Query(PRIVATE_QUOTES_QUERY)
    Page<Quote> findAllByStatusName(QuoteStatusName statusName, Pageable p);

    // Find private quotes by keyword
    @Query(FIND_BY_STATUS_NAME_AND_KEYWORD + PRIVATE_QUOTES_BY_KEYWORD_CONDITION)
    Page<Quote> findPrivateByStatusNameAndKeyword(QuoteStatusName statusName, String keyword, Pageable p);

    // Find all public quotes and sort by interesting tags
    @Query(value = INTERESTING_TAGS_QUERY_START +
            INTERESTING_TAGS_PUBLIC_QUERY_CENTER +
            INTERESTING_TAGS_QUERY_END,
            nativeQuery = true)
    Page<Quote> findPublicQuotesSortByInterestingTags(Long principalId, Pageable p);

    // Find all pending quotes and sort by interesting tags
    @Query(value = INTERESTING_TAGS_QUERY_START +
            INTERESTING_TAGS_PENDING_QUERY_CENTER +
            INTERESTING_TAGS_QUERY_END,
            nativeQuery = true)
    Page<Quote> findPendingQuotesSortByInterestingTags(Long principalId, Pageable p);

    // Find all private quotes and sort by interesting tags
    @Query(value = INTERESTING_TAGS_QUERY_START +
            INTERESTING_TAGS_PRIVATE_QUERY_CENTER +
            INTERESTING_TAGS_QUERY_END,
            nativeQuery = true)
    Page<Quote> findPrivateQuotesSortByInterestingTags(Long principalId, Pageable p);

    // Find public quotes by keyword and sort by interesting tags
    @Query(value = INTERESTING_TAGS_QUERY_START +
            INTERESTING_TAGS_KEYWORD_CENTER_START +
            INTERESTING_TAGS_PUBLIC_QUERY_CENTER +
            INTERESTING_TAGS_KEYWORD_CENTER_END +
            INTERESTING_TAGS_QUERY_END,
            nativeQuery = true)
    Page<Quote> findPublicQuotesByKeywordSortByInterestingTags(Long principalId, String keyword, Pageable p);

    // Find pending quotes by keyword and sort by interesting tags
    @Query(value = INTERESTING_TAGS_QUERY_START +
            INTERESTING_TAGS_KEYWORD_CENTER_START +
            INTERESTING_TAGS_PENDING_QUERY_CENTER +
            INTERESTING_TAGS_KEYWORD_CENTER_END +
            INTERESTING_TAGS_QUERY_END,
            nativeQuery = true)
    Page<Quote> findPendingQuotesByKeywordSortByInterestingTags(Long principalId, String keyword, Pageable p);

    // Find private quotes by keyword and sort by interesting tags
    @Query(value = INTERESTING_TAGS_QUERY_START +
            INTERESTING_TAGS_KEYWORD_CENTER_START +
            INTERESTING_TAGS_PRIVATE_QUERY_CENTER +
            INTERESTING_TAGS_KEYWORD_CENTER_END +
            INTERESTING_TAGS_QUERY_END,
            nativeQuery = true)
    Page<Quote> findPrivateQuotesByKeywordSortByInterestingTags(Long principalId, String keyword, Pageable p);

    // Find quote by id
    @NonNull
    Optional<Quote> findById(@NonNull Long id);
}
