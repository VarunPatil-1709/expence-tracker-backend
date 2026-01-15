package com.jobportal.system.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.Builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobportal.system.entity.RefreshToken;
import com.jobportal.system.entity.UserInfo;

import com.jobportal.system.repo.RefreshTokenRepository;
import com.jobportal.system.repo.UserRepository;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    public RefreshToken createRefreshToken(String username) {

        UserInfo user = userRepository.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("User not found: " + username);
        }

        // Check existing token for this user
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUserInfo(user);
 
        if (existingToken.isPresent()) {
            // Update existing token
            RefreshToken token = existingToken.get();
            token.setToken(UUID.randomUUID().toString());
            token.setExpiryDate(Instant.now().plusMillis(6000000));
            return refreshTokenRepository.save(token);
        }

        // Create new token for first-time login
        RefreshToken newToken = RefreshToken.builder()
                .userInfo(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(6000000))
                .build();

        return refreshTokenRepository.save(newToken);
    }


    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }

}