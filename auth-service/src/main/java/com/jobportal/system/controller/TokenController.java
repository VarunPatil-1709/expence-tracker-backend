package com.jobportal.system.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.system.entity.RefreshToken;
import com.jobportal.system.request.AuthRequestDto;
import com.jobportal.system.request.RequestTokenRequestDto;
import com.jobportal.system.responce.JwtResponseDTO;
import com.jobportal.system.service.JwtService;
import com.jobportal.system.service.RefreshTokenService;
import com.jobportal.system.service.UserDetailsServiceImp;


@RestController
public class TokenController
{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserDetailsServiceImp userDetailsService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("auth/v1/login")
    public ResponseEntity AuthenticateAndGetToken(@RequestBody AuthRequestDto authRequestDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequestDTO.getUsername(),
                        authRequestDTO.getPassword()
                )
        );

        if (authentication.isAuthenticated()) {

            RefreshToken refreshToken =
                    refreshTokenService.createRefreshToken(authRequestDTO.getUsername());

            String userId =
                    userDetailsService.getUserByUsername(authRequestDTO.getUsername());

            return ResponseEntity.ok(
                    JwtResponseDTO.builder()
                            .accessToken(jwtService.GenerateToken(authRequestDTO.getUsername()))
                            .token(refreshToken.getToken())
                            .userId(userId)     // 🔥 THIS WAS MISSING
                            .build()
            );
        }

        return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PostMapping("auth/v1/refreshToken")
    public ResponseEntity<JwtResponseDTO> refreshToken(@RequestBody RequestTokenRequestDto refreshTokenRequestDTO){

    	String token = refreshTokenRequestDTO.getToken().trim();
        return refreshTokenService.findByToken(token)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String accessToken = jwtService.GenerateToken(userInfo.getUsername());
                    return ResponseEntity.ok(
                            JwtResponseDTO.builder()
                                    .accessToken(accessToken)
                                    .token(token)
                                    .userId(userInfo.getUserId())
                                    .build()
                    );
                }).orElseThrow(() ->new RuntimeException("Refresh Token is not in DB..!!"));

    }
}