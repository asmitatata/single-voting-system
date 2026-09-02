package com.hamropatro.mapper;

import com.hamropatro.Users;
import com.hamropatro.dto.authenticationDto.RegisterDto;
import com.hamropatro.dto.userDto.UserDto;
import com.hamropatro.entity.UserEntity;
import jakarta.inject.Singleton;

import java.time.Instant;
import java.util.List;

@Singleton
public class AuthUserMapper {

    public UserEntity fromRegisterDtoToUserEntity(RegisterDto registerDto) {
        return UserEntity.builder()
                .userId(registerDto.userId())
                .email(registerDto.email())
                .name(registerDto.name())
                .password(registerDto.password())
                .role(registerDto.role())
                .isActive(registerDto.isActive())
                .createdAt(Instant.now().toEpochMilli())
                .updatedAt(Instant.now().toEpochMilli())
                .build();
    }

    public UserDto fromUserEntityToUserDto(UserEntity userEntity) {
        return UserDto.builder()
                .userId(userEntity.getUserId())
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .password(userEntity.getPassword())
                .role(userEntity.getRole())
                .isActive(userEntity.isActive())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .build();
    }

    public static Users fromUserDtoToUsers(UserDto userDto) {
        return Users.newBuilder()
                .setUserId(String.valueOf(userDto.userId()))
                .setUserEmail(userDto.email())
                .setUserName(userDto.name())
                .build();
    }

    public List<UserDto> fromUserEntitiesToUserDto(
            List<UserEntity> userEntities) {

        return userEntities.stream()
                .map(this::fromUserEntityToUserDto)
                .toList();
    }

}
