package com.hamropatro.service.implementation;

import com.hamropatro.dto.authenticationDto.LoginDto;
import com.hamropatro.dto.authenticationDto.LoginResponseDto;
import com.hamropatro.dto.authenticationDto.RegisterDto;
import com.hamropatro.dto.userDto.UserDto;
import com.hamropatro.entity.UserEntity;
import com.hamropatro.enums.Role;
import com.hamropatro.exceptions.InvalidCredentialException;
import com.hamropatro.jwt.GoogleVerifier;
import com.hamropatro.jwt.JwtUtil;
import com.hamropatro.mapper.AuthUserMapper;
import com.hamropatro.repository.AuthRepository;
import com.hamropatro.service.AuthServiceDetail;
import jakarta.inject.Singleton;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.UUID;

@Singleton
public class AuthServiceDetailImplementation implements AuthServiceDetail {
    private final AuthUserMapper authUserMapper;
    private final AuthRepository authRepository;
    private final JwtUtil jwtUtil;
    private final GoogleVerifier googleVerifier;

    public AuthServiceDetailImplementation(AuthUserMapper authUserMapper,
                                           AuthRepository authRepository,
                                           JwtUtil jwtUtil,
                                           GoogleVerifier googleVerifier
    ) {
        this.authUserMapper = authUserMapper;
        this.authRepository = authRepository;
        this.jwtUtil = jwtUtil;
        this.googleVerifier = googleVerifier;
    }

    @Override
    public void register(RegisterDto registerDto) {
        var entity = authUserMapper.fromRegisterDtoToUserEntity(registerDto);
        entity.setUserId(UUID.randomUUID());
        entity.setPassword(
                BCrypt.hashpw(entity.getPassword(), BCrypt.gensalt())
        );
        authRepository.register(entity);
    }

    @Override
    public LoginResponseDto login(LoginDto loginDto) {
        var user = authRepository.findByEmail(loginDto.email())
                .orElseThrow(() ->
                        new InvalidCredentialException("Invalid email or password")
                );

        if (!BCrypt.checkpw(loginDto.password(), user.password())) {
            throw new InvalidCredentialException("Invalid email or password");
        }
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        return new LoginResponseDto(accessToken, refreshToken);
    }

    @Override
    public LoginResponseDto refresh(UserDto userDto) {

        String accessToken = jwtUtil.generateAccessToken(userDto);
        String refreshToken = jwtUtil.generateRefreshToken(userDto);
        return new LoginResponseDto(accessToken, refreshToken);
    }

    @Override
    public LoginResponseDto googleLogin(String idToken) {
        try {
            var googleToken = googleVerifier.verify(idToken);
            if (googleToken == null) {
                throw new InvalidCredentialException("Invalid Google ID token");
            }
            var payload = googleToken.getPayload();

            String email = payload.getEmail();
            String name = (String) payload.get("name");

            var user = authRepository.findByEmail(email);

            if (user.isEmpty()) {
                UserEntity entity = UserEntity.builder()
                        .userId(UUID.randomUUID())
                        .email(email)
                        .name(name)
                        .role(Role.VOTER)
                        .isActive(true)
                        .createdAt(Instant.now().toEpochMilli())
                        .updatedAt(Instant.now().toEpochMilli())
                        .build();

                authRepository.register(entity);

                user = authRepository.findByEmail(email);
            }

            var userDto = user.orElseThrow(() ->
                    new RuntimeException("User not found")
            );

            String accessToken = jwtUtil.generateAccessToken(userDto);
            String refreshToken = jwtUtil.generateRefreshToken(userDto);

            return new LoginResponseDto(accessToken, refreshToken);

        }
        catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException("Google token verification failed", e);
        }
    }
}
