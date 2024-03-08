package com.w1zer.service;

import com.w1zer.entity.Profile;
import com.w1zer.exception.NotFoundException;
import com.w1zer.exception.ProfileAlreadyExistsException;
import com.w1zer.mapping.ProfileMapper;
import com.w1zer.payload.ProfileRequest;
import com.w1zer.payload.ProfileResponse;
import com.w1zer.repository.ProfileRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProfileService {
    private static final String PROFILE_WITH_LOGIN_ALREADY_EXISTS = "Profile with login '%s' already exists";
    private static final String PROFILE_WITH_ID_NOT_FOUND = "Profile with id '%d' not found";
    private static final String PROFILE_WITH_LOGIN_NOT_FOUND = "Profile with login '%s' not found";

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final PasswordEncoder passwordEncoder;

    public ProfileService(ProfileRepository profileRepository, ProfileMapper profileMapper,
                          PasswordEncoder passwordEncoder) {
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<ProfileResponse> getAll(String login) {
        if (login == null) {
            return profileMapper.mapToProfileResponseList(profileRepository.findAll());
        }
        Profile profile = profileRepository.findByLogin(login).orElseThrow(
                () -> new NotFoundException(PROFILE_WITH_LOGIN_NOT_FOUND.formatted(login))
        );
        return profileMapper.mapToProfileResponseList(profile);
    }

    public ProfileResponse getById(Long id) {
        Profile profile = profileRepository.findById(id).orElseThrow(
                () -> new NotFoundException(PROFILE_WITH_ID_NOT_FOUND.formatted(id))
        );
        return profileMapper.mapToProfileResponse(profile);
    }

    public ProfileResponse insert(ProfileRequest profileRequest) {
        String login = profileRequest.login();
        if (profileRepository.existsByLogin(login)) {
            throw new ProfileAlreadyExistsException(PROFILE_WITH_LOGIN_ALREADY_EXISTS.formatted(login));
        }
        Profile profile = getProfileWithEncodedPassword(profileRequest);
        Profile result = profileRepository.save(profile);
        return profileMapper.mapToProfileResponse(result);
    }

    public void delete(Long id) {
        if (!profileRepository.existsById(id)) {
            throw new NotFoundException(PROFILE_WITH_ID_NOT_FOUND.formatted(id));
        }
        profileRepository.deleteById(id);
    }

    public ProfileResponse update(Long id, ProfileRequest profileRequest) {
        if (!profileRepository.existsById(id)) {
            throw new NotFoundException(PROFILE_WITH_ID_NOT_FOUND.formatted(id));
        }
        String login = profileRequest.login();
        Optional<Profile> profileWithSameLogin = profileRepository.findByLogin(login);
        if (profileWithSameLogin.isPresent() && !Objects.equals(profileWithSameLogin.get().getId(), id)) {
            throw new ProfileAlreadyExistsException(PROFILE_WITH_LOGIN_ALREADY_EXISTS.formatted(login));
        }
        Profile profile = getProfileWithEncodedPasswordAndId(id, profileRequest);
        Profile result = profileRepository.save(profile);
        return profileMapper.mapToProfileResponse(result);
    }

    public Profile getProfileById(Long idProfile){
        return profileRepository.findById(idProfile).orElseThrow(
                () -> new NotFoundException(PROFILE_WITH_ID_NOT_FOUND.formatted(idProfile))
        );
    }

    private Profile getProfileWithEncodedPassword(ProfileRequest profileRequest) {
        Profile profile = profileMapper.mapToProfile(profileRequest);
        profile.setPassword(passwordEncoder.encode(profile.getPassword()));
        return profile;
    }

    private Profile getProfileWithEncodedPasswordAndId(Long id, ProfileRequest profileRequest) {
        Profile profile = profileMapper.mapToProfile(id, profileRequest);
        profile.setPassword(passwordEncoder.encode(profile.getPassword()));
        return profile;
    }
}
