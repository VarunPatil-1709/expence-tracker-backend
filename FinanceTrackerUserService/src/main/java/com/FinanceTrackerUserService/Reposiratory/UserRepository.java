package com.FinanceTrackerUserService.Reposiratory;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.FinanceTrackerUserService.Entity.UserInfo;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface UserRepository extends CrudRepository<UserInfo, Long>
{

    Optional<UserInfo> findByUserId(String userId);

}