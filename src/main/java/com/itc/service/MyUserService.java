package com.itc.service;

import com.itc.exception.UsernameNotFoundException;
import com.itc.model.MyUsers;




public interface MyUserService {
	
	MyUsers addUser(MyUsers user);

	String generateTemporaryPassword(String username) throws UsernameNotFoundException;

	void resetPassword(String username, String tempPassword, String newPassword) throws Throwable;

	MyUsers findByUsername(String username) throws UsernameNotFoundException;

	 
}
