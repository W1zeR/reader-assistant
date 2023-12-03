package com.w1zer.security;

import com.w1zer.entity.Profile;
import com.w1zer.exception.NotFoundException;
import com.w1zer.repository.ProfileRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static final String PROFILE_WITH_LOGIN_NOT_FOUND = "Profile with login '%s' not found";

    private final ProfileRepository profileRepository;

    public CustomUserDetailsService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Profile> profile = profileRepository.findByLogin(username);
        return profile.map(CustomUserDetails::new).orElseThrow(
                () -> new NotFoundException(PROFILE_WITH_LOGIN_NOT_FOUND.formatted(username))
        );
    }
}
