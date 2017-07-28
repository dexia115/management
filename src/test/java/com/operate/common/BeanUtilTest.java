package com.operate.common;

import com.operate.pojo.account.User;

public class BeanUtilTest {
	public static void main(String[] args) throws Exception {
//		BeanUtils beanUtils = new BeanUtils();
//		beanUtils.createBeanDao(User.class, "account");
//		beanUtils.createBeanDaoImpl(User.class, "account");
//		beanUtils.createBeanController(User.class, "account");
//		beanUtils.createBeanJavascript(User.class, "account");
//		beanUtils.createBeanHtml(User.class, "account");
		
		String name = "parent.id";
		String[] names = name.split("[.]");
		System.out.println(names.length);
	}

}
