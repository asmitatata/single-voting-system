package com.hamropatro.service;

import com.hamropatro.dto.userDto.UserDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDetail {
    Optional<UserDto> findById(UUID userId);

    List<UserDto> findAll();
}
