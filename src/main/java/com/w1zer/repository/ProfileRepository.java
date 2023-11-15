package com.w1zer.repository;

import com.w1zer.entity.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends CrudRepository<Profile, Long> {
    @Override
    @NonNull
    List<Profile> findAll();

    boolean existsByLogin(String login);

    Optional<Profile> findProfileById(Long id);

    Optional<Profile> findProfileByLogin(String login);
}
