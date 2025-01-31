package com.itc.service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.itc.exception.UsernameNotFoundException;
import com.itc.model.MyUsers;
import com.itc.repository.MyUserRepo;


@Service
public class MyUserServiceImpl implements MyUserService {
    private static final Logger LOGGER = Logger.getLogger(MyUserServiceImpl.class.getName());
	@Autowired
	MyUserRepo repo;
	
	@Autowired
	PasswordEncoder encoder;
	
	public MyUsers addUser(MyUsers user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    public MyUsers findByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("Finding user by username: " + username);
        Optional<MyUsers> optionalUser = repo.findByUserName(username);
        return optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public void save(MyUsers user) {
        repo.save(user);
    }

    public String generateTemporaryPassword(String username) throws UsernameNotFoundException {
        MyUsers user = findByUsername(username);
        String tempPassword = UUID.randomUUID().toString().substring(0, 8);
        user.setPassword(encoder.encode(tempPassword));
        save(user);
        return tempPassword; 
    }

    public void resetPassword(String username, String tempPassword, String newPassword) throws UsernameNotFoundException {
        MyUsers user = findByUsername(username);
        if (encoder.matches(tempPassword, user.getPassword())) {
            user.setPassword(encoder.encode(newPassword));
            save(user);
        } else {
            throw new UsernameNotFoundException("Invalid temporary password provided for username: " + username);
        }
    }	

}