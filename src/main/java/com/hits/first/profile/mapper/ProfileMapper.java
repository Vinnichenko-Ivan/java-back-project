package com.hits.first.profile.mapper;


import com.hits.first.profile.dto.ProfileDto;
import com.hits.first.profile.dto.ProfileEditDto;
import com.hits.first.profile.dto.ProfileRegisterDto;
import com.hits.first.profile.model.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    Profile map(ProfileRegisterDto profileRegisterDto);


    void map(@MappingTarget Profile profile, ProfileEditDto profileRegisterDto);

    ProfileDto map(Profile profile);
}
