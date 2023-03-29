package com.hits.first.profile.service.impl;

import com.hits.first.exception.BadCredentialsException;
import com.hits.first.exception.ProfileAlreadyExistException;
import com.hits.first.exception.ProfileNotFoundException;
import com.hits.first.profile.dto.CredentialsDto;
import com.hits.first.profile.dto.ProfileDto;
import com.hits.first.profile.dto.ProfileEditDto;
import com.hits.first.profile.dto.ProfileRegisterDto;
import com.hits.first.profile.mapper.ProfileMapper;
import com.hits.first.profile.model.Profile;
import com.hits.first.profile.repository.ProfileRepository;
import com.hits.first.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    @Override
    public void register(ProfileRegisterDto profileRegisterDto) {
        if(profileRepository.existsByLogin(profileRegisterDto.getLogin())) {
            throw new ProfileAlreadyExistException();
        }
        else {
            Profile profile = profileMapper.map(profileRegisterDto);
            profileRepository.save(profile);
        }
    }

    @Override
    public ProfileDto getUser(CredentialsDto credentialsDto) {
        if(profileRepository.existsByLogin(credentialsDto.getLogin())) {
            Profile profile = profileRepository.getByLogin(credentialsDto.getLogin());
            if(profile.getPassword().equals(credentialsDto.getPassword()))
            {
                return profileMapper.map(profile);
            }
            else
            {
                throw new BadCredentialsException();
            }
        }
        else {
            throw new BadCredentialsException();
        }
    }

    @Override
    public void putUser(ProfileEditDto profileEditDto) {
        if(profileRepository.existsByLogin(profileEditDto.getLogin())) {
            Profile profile = profileRepository.getByLogin(profileEditDto.getLogin());
            if(profile.getPassword().equals(profileEditDto.getPassword()))
            {
                profileMapper.map(profile, profileEditDto);
                profileRepository.save(profile);
            }
            else
            {
                throw new BadCredentialsException();
            }
        }
        else {
            throw new BadCredentialsException();
        }
    }
}
