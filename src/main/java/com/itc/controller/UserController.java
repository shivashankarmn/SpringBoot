package com.itc.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itc.exception.UsernameNotFoundException;
import com.itc.model.AuthRequest;
import com.itc.model.MyUsers;
import com.itc.service.JwtService;
import com.itc.service.MyUserService;

@RestController
@RequestMapping("/api")
public class UserController {
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	MyUserService serv;
	
	@Autowired
	JwtService jwtService;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@PostMapping("/new")
	public ResponseEntity<?> registerUser(@RequestBody MyUsers users)
	{

         
		MyUsers user = serv.addUser(users);
		return new ResponseEntity<MyUsers>(user, HttpStatus.OK);
	}
	
	@PostMapping("/authenticate")
	public String authenticate(@RequestBody AuthRequest authRequest) throws UsernameNotFoundException
	{
			
		Authentication authenticate = authenticationManager
		.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		
		if(authenticate.isAuthenticated())
		{
			return jwtService.generateToken(authRequest.getUsername());
		}
		else
		{
			throw new UsernameNotFoundException("invalid Credentials");
		}
	
	
		
	}
	

	@PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody String username) throws JsonMappingException, JsonProcessingException {
        try {
        	

            JsonNode jsonNode = objectMapper.readTree(username);
            String userName = jsonNode.get("username").asText();
            
            MyUsers user = serv.findByUsername(userName);
            
            return new ResponseEntity<>("Username found. Confirm to receive a temporary password.", HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/confirm-forgot-password")
    public ResponseEntity<String> confirmForgotPassword(@RequestBody String username) throws JsonMappingException, JsonProcessingException {
        try {
        	JsonNode jsonNode = objectMapper.readTree(username);
            String userName = jsonNode.get("username").asText();
            
            String tempPassword = serv.generateTemporaryPassword(userName);
            return new ResponseEntity<>("Temporary password " + tempPassword, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) throws Throwable {
        try {
            serv.resetPassword(passwordResetRequest.getUsername(), passwordResetRequest.getTempPassword(), passwordResetRequest.getNewPassword());
            return new ResponseEntity<>("Password reset successful", HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }}












