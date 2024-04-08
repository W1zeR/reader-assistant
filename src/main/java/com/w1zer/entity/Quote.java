package com.w1zer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.w1zer.constants.EntityConstants.CONTENT_LENGTH;

@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = CONTENT_LENGTH, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    @ToString.Exclude
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    @ToString.Exclude
    private QuoteStatus quoteStatus;

    @ManyToMany(mappedBy = "likedQuotes")
    @ToString.Exclude
    @JsonIgnore
    private Set<Profile> whoLiked = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "quotes_tags", joinColumns = @JoinColumn(name = "id_quote"),
            inverseJoinColumns = @JoinColumn(name = "id_tag"))
    @ToString.Exclude
    @JsonIgnore
    private Set<Tag> tags = new HashSet<>();

    public void addLikedProfile(Profile profile) {
        this.whoLiked.add(profile);
        profile.getLikedQuotes().add(this);
    }

    public void removeLikedProfile(Profile profile) {
        this.whoLiked.remove(profile);
        profile.getLikedQuotes().remove(this);
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getQuotes().add(this);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getQuotes().remove(this);
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
        Quote quote = (Quote) o;
        return getId() != null && Objects.equals(getId(), quote.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ?
                ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() :
                getClass().hashCode();
    }
}
