package com.jobportal.system.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jobportal.system.entity.RefreshToken;
import com.jobportal.system.entity.UserInfo;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Integer>
{
    Optional<RefreshToken> findByToken(String token);

	Optional<RefreshToken> findByUserInfo(UserInfo user);

}