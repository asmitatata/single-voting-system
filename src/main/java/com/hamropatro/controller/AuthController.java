package com.hamropatro.controller;

import com.hamropatro.*;
import com.hamropatro.dto.authenticationDto.LoginDto;
import com.hamropatro.dto.authenticationDto.LoginResponseDto;
import com.hamropatro.dto.authenticationDto.RegisterDto;
import com.hamropatro.enums.Role;
import com.hamropatro.exceptions.InvalidCredentialException;
import com.hamropatro.jwt.JwtValidator;
import com.hamropatro.repository.AuthRepository;
import com.hamropatro.service.AuthServiceDetail;
import com.hamropatro.validation.Validate;
import io.grpc.stub.StreamObserver;
import io.jsonwebtoken.Claims;
import io.micronaut.context.annotation.Value;
import io.micronaut.grpc.annotation.GrpcService;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@GrpcService
@Slf4j
public class AuthController extends AuthServiceGrpc.AuthServiceImplBase {

    private final AuthServiceDetail authServiceDetail;
    private final JwtValidator jwtValidator;
    private final AuthRepository authRepository;
    private final List<String> adminEmailList;
    private final Validate<RegisterDto> validateRegister;

    public AuthController(AuthServiceDetail authServiceDetail, JwtValidator jwtValidator, AuthRepository authRepository,
                          @Value("${voting.admin-email}") String adminEmails, Validate<RegisterDto> validateRegister
    ) {
        this.authServiceDetail = authServiceDetail;
        this.jwtValidator = jwtValidator;
        this.authRepository = authRepository;
        this.adminEmailList = Arrays.asList(adminEmails.split(","));
        this.validateRegister = validateRegister;
    }


    @Override
    public void register(RegisterRequest request, StreamObserver<RegisterResponse> responseObserver) {
        Role role = adminEmailList.stream()
                .map(String::trim)
                .anyMatch(email ->
                        email.equalsIgnoreCase(request.getEmail().trim())
                )
                ? Role.ADMIN
                : Role.VOTER;

        RegisterDto dto = RegisterDto.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(request.getPassword())
                .role(role)
                .build();

        validateRegister.validate(dto);
        authServiceDetail.register(dto);
        RegisterResponse response = RegisterResponse.newBuilder()
                .setMessage("User Registered Successfully")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        var dto = new LoginDto(
                request.getEmail(),
                request.getPassword()
        );
        LoginResponseDto tokenResponse = authServiceDetail.login(dto);
        var response = LoginResponse.newBuilder()
                .setAccessToken(tokenResponse.accessToken())
                .setRefreshToken(tokenResponse.refreshToken())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void loginRefresh(RefreshRequest request, StreamObserver<LoginResponse> responseObserver) {
        String refreshToken = request.getRefreshToken();

        Claims claims = jwtValidator.validateRefreshToken(refreshToken);

        String userId = claims.getSubject();
        String email = claims.get("email", String.class);
        String name = claims.get("name", String.class);
        String role = claims.get("role", String.class);

        var user = authRepository.findById(UUID.fromString(userId))
                .orElseThrow(() ->
                        new InvalidCredentialException("User Not Found")
                );

        LoginResponseDto tokenResponse = authServiceDetail.refresh(user);
        var response = LoginResponse.newBuilder()
                .setAccessToken(tokenResponse.accessToken())
                .setRefreshToken(tokenResponse.refreshToken())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void googleLogin(GoogleLoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        LoginResponseDto loginResponse = authServiceDetail.googleLogin(request.getIdToken());

        LoginResponse response = LoginResponse.newBuilder()
                .setAccessToken(loginResponse.accessToken())
                .setRefreshToken(loginResponse.refreshToken())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}

