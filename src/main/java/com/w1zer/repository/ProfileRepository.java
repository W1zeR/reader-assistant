package com.w1zer.repository;

import com.w1zer.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    boolean existsByLogin(String login);

    boolean existsByEmail(String email);

    Optional<Profile> findByLogin(String login);
}
