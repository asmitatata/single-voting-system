package com.hamropatro.service.implementation;

import com.hamropatro.dto.userDto.UserDto;
import com.hamropatro.mapper.AuthUserMapper;
import com.hamropatro.repository.UserRepository;
import com.hamropatro.service.UserDetail;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class UserDetailImplementation implements UserDetail {
    private final AuthUserMapper userMapper;
    private final UserRepository userRepository;

    public UserDetailImplementation(AuthUserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserDto> findById(UUID userId) {

        var entity = userRepository.findById(userId);

        var users = entity.map(
                userMapper::fromUserEntityToUserDto
        );
        return users;
    }

    @Override
    public List<UserDto> findAll() {
        var entity = userRepository.findAll();

        var users = userMapper.fromUserEntitiesToUserDto(entity);
        return users;
    }
}
