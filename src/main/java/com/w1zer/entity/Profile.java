package com.w1zer.entity;

import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.w1zer.constants.EntityConstants.EMAIL_LENGTH;
import static com.w1zer.constants.EntityConstants.LOGIN_LENGTH;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Profile implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = EMAIL_LENGTH, nullable = false, unique = true)
    private String email;

    @Column(length = LOGIN_LENGTH, nullable = false, unique = true)
    private String login;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "profiles_roles", joinColumns = @JoinColumn(name = "id_profile"),
            inverseJoinColumns = @JoinColumn(name = "id_role"))
    @ToString.Exclude
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Quote> quotes = new ArrayList<>();

    public void addQuote(Quote quote){
        this.quotes.add(quote);
        quote.setProfile(this);
    }
    public void removeQuote(Quote quote){
        this.quotes.remove(quote);
        quote.setProfile(null);
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
        Profile profile = (Profile) o;
        return getId() != null && Objects.equals(getId(), profile.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ?
                ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() :
                getClass().hashCode();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
