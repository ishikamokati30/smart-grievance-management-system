package com.ishika.grievance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ishika.grievance.dto.LoginRequest;
import com.ishika.grievance.dto.RegisterRequest;
import com.ishika.grievance.entity.User;
import com.ishika.grievance.repository.UserRepository;

@Service
public class AuthService {
	
	@Autowired
	private UserRepository userRepository;
	
	 public String register(RegisterRequest request) {

	        User user = new User();

	        user.setName(request.getName());
	        user.setEmail(request.getEmail());
	        user.setPassword(request.getPassword());

	        user.setRole("USER");

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

		    if(!user.getPassword().equals(request.getPassword())) {
		        return "Invalid Password";
		    }

		    return "Login Successful";
		}

}
