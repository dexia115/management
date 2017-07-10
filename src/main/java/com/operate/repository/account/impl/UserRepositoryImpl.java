package com.operate.repository.account.impl;

import com.operate.repository.HibernateRepositoryImpl;
import org.springframework.stereotype.Repository;
import com.operate.repository.account.UserRepository;
import com.operate.pojo.account.User;

@Repository
public class UserRepositoryImpl extends HibernateRepositoryImpl<User> implements UserRepository {

}