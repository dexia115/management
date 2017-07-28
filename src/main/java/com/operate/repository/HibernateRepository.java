package com.operate.repository;

import java.util.List;
import com.operate.tools.Groups;
import com.operate.tools.PageObj;

public interface HibernateRepository<T>{
	
	PageObj<T> findPageByGroups(Groups groups, PageObj<T> page, String sql, Class<?> classes);
	
	PageObj<T> findPageByGroupsNoSplit(Groups groups, PageObj<T> page, String sql, Class<?> classes);
	
	@SuppressWarnings("rawtypes")
	List findByGroups(Groups groups, String sql, Class<?> classes);
}
