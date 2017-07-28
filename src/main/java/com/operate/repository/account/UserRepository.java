package com.operate.repository.account;

import com.operate.repository.CustomRepository;
import org.springframework.cache.annotation.CacheConfig;

import com.operate.pojo.account.User;

@CacheConfig(cacheNames = "user")
public interface UserRepository extends CustomRepository<User,Long>{

}