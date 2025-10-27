package com.w1zer.service;

import com.w1zer.entity.Profile;
import com.w1zer.repository.ProfileRepository;
import com.w1zer.security.UserPrincipal;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static final String PROFILE_NOT_FOUND = "Refresh token '%s' not found";

    private final ProfileRepository profileRepository;

    public CustomUserDetailsService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Profile profile = profileRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException(PROFILE_NOT_FOUND.formatted(username))
        );
        return UserPrincipal.profileToUserPrincipal(profile);
    }
}
