package com.ishika.grievance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ishika.grievance.dto.LoginRequest;
import com.ishika.grievance.dto.RegisterRequest;
import com.ishika.grievance.entity.User;
import com.ishika.grievance.repository.UserRepository;
import com.ishika.grievance.security.JwtUtil;
import com.ishika.grievance.enums.Role;
@Service
public class AuthService {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	 public String register(RegisterRequest request) {

	        User user = new User();

	        user.setName(request.getName());
	        user.setEmail(request.getEmail());
	        user.setPassword(
	                passwordEncoder.encode(
	                        request.getPassword()));

	        user.setRole(Role.USER);

	        userRepository.save(user);

	        return "User Registered Successfully";
	    }
	 
	 public String login(LoginRequest request) {

		    User user = userRepository
		                    .findByEmail(request.getEmail())
		                    .orElse(null);

		    if(user == null) {
		        return "User Not Found";
		    }

		    if(!passwordEncoder.matches(
		            request.getPassword(),
		            user.getPassword())) {

		        return "Invalid Password";
		    }

		    return jwtUtil.generateToken(user.getEmail());
		}

}
