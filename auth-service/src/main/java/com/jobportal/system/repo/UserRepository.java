package com.jobportal.system.repo;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jobportal.system.entity.UserInfo;

@Repository
public interface UserRepository extends CrudRepository<UserInfo, String>
{
    public UserInfo findByUsername(String username);
}