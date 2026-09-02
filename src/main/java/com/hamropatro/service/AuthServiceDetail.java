package com.hamropatro.service;


import com.hamropatro.dto.authenticationDto.LoginDto;
import com.hamropatro.dto.authenticationDto.LoginResponseDto;
import com.hamropatro.dto.authenticationDto.RegisterDto;
import com.hamropatro.dto.userDto.UserDto;

public interface AuthServiceDetail {

    void register(RegisterDto registerDto);

    LoginResponseDto login(LoginDto loginDto);

    LoginResponseDto refresh(UserDto userDto);

    LoginResponseDto googleLogin(String idToken);
}
