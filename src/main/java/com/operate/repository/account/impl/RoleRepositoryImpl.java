package com.operate.repository.account.impl;

import org.springframework.stereotype.Repository;
import com.operate.pojo.account.Role;
import com.operate.repository.HibernateRepositoryImpl;
import com.operate.repository.account.RoleRepositoryCustom;

@Repository
public class RoleRepositoryImpl extends HibernateRepositoryImpl<Role> implements RoleRepositoryCustom {


}
