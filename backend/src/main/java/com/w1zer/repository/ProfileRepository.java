package com.w1zer.repository;

import com.w1zer.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    @Override
    @NonNull
    List<Profile> findAll();

    boolean existsByEmail(String email);

    boolean existsByLogin(String login);

    Optional<Profile> findByEmail(String login);
}
