package com.operate.repository.account.impl;

import org.springframework.stereotype.Repository;
import com.operate.pojo.account.Authority;
import com.operate.repository.HibernateRepositoryImpl;
import com.operate.repository.account.AuthorityRepository;

@Repository
public class AuthorityRepositoryImpl extends HibernateRepositoryImpl<Authority> implements AuthorityRepository {

}
