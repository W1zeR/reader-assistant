package com.w1zer.entity;

import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class QuoteStatus {
    private static final int QUOTE_STATUS_NAME_LENGTH = 7;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = QUOTE_STATUS_NAME_LENGTH, nullable = false)
    private QuoteStatusName name;

    @OneToMany(mappedBy = "quoteStatus", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Quote> quotes = new ArrayList<>();

    public void addQuote(Quote quote){
        this.quotes.add(quote);
        quote.setQuoteStatus(this);
    }
    public void removeQuote(Quote quote){
        this.quotes.remove(quote);
        quote.setQuoteStatus(null);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ?
                ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ?
                ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Role role = (Role) o;
        return getId() != null && Objects.equals(getId(), role.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ?
                ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() :
                getClass().hashCode();
    }
}
