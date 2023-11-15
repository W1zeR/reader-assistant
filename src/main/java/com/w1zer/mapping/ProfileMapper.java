package com.w1zer.mapping;

import com.w1zer.entity.Profile;
import com.w1zer.model.ProfileRequest;
import com.w1zer.model.ProfileResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProfileMapper {
    public ProfileResponse mapToProfileResponse(Profile profile) {
        return ProfileResponse
                .builder()
                .id(profile.getId())
                .login(profile.getLogin())
                .build();
    }

    public Profile mapToProfile(ProfileRequest profileRequest) {
        return Profile
                .builder()
                .login(profileRequest.getLogin())
                .password(profileRequest.getPassword())
                .build();
    }

    public Profile mapToProfile(Long id, ProfileRequest profileRequest) {
        return Profile
                .builder()
                .id(id)
                .login(profileRequest.getLogin())
                .password(profileRequest.getPassword())
                .build();
    }

    public List<ProfileResponse> mapToProfileResponseList(List<Profile> profiles) {
        return profiles
                .stream()
                .map(this::mapToProfileResponse)
                .collect(Collectors.toList());
    }

    public List<ProfileResponse> mapToProfileResponseList(Profile profile) {
        return List.of(mapToProfileResponse(profile));
    }
}
