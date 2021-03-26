package com.manohar.kisansevapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manohar.kisansevapp.config.JwtTokenUtil;
import com.manohar.kisansevapp.model.JwtRequest;
import com.manohar.kisansevapp.model.JwtResponse;
import com.manohar.kisansevapp.model.User;
import com.manohar.kisansevapp.model.UserDetails;
import com.manohar.kisansevapp.model.UserProfile;
import com.manohar.kisansevapp.service.UserService;
import com.manohar.kisansevapp.util.ResourceAlreadyExists;
import com.manohar.kisansevapp.util.ResourceNotFoundException;

@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	PasswordEncoder encoder;
	
	/* User Login Request */
	@PostMapping("/auth/login")
	public ResponseEntity<JwtResponse> LoginUser(@RequestBody JwtRequest authRequest) throws Exception {
		try {
			authenticate(authRequest.getUserName(), authRequest.getPassword());

		} catch (Exception ex) {
			throw new ResourceNotFoundException("inavalid username/password");
		}

		final String token = jwtTokenUtil.generateToken(authRequest.getUserName());

		return ResponseEntity.ok(new JwtResponse(token));

	}

	@PostMapping("/auth/signup")
	public ResponseEntity<?> createUser(@RequestBody User user) {
		System.out.println("inside createUser");

		if (userService.isUserExist(user)) {
			throw new ResourceAlreadyExists(
					"Unable to create. A User with user name " + user.getUserName() + " or mobile " + user.getMobile() + " already exist.");
		}
		user.setPassword(encoder.encode(user.getPassword()));
		userService.saveUser(user);
		HttpHeaders headers = new HttpHeaders();
		System.out.println("user saved");
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@GetMapping("/user/{userName}")
	public ResponseEntity<?> getUserById(@PathVariable("userName") String username) {

		User user = userService.findByUserName(username);

		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping("/user/mobile/{mobile}")
	public ResponseEntity<?> getUserByMobile(@PathVariable("mobile") Long mobile) {

		Optional<User> u = userService.findByMobile(mobile);
		if (!u.isPresent()) {
			throw new ResourceNotFoundException("User not found with mobile number = " + mobile);
		}
		return new ResponseEntity<>(u, HttpStatus.OK);
	}

	@GetMapping("/user/getAllUser")
	public ResponseEntity<List<User>> getAllUser() {
		System.out.println("Request here");
		List<User> user = userService.findAllUser();
		if (user.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		System.out.println(user);
		return new ResponseEntity<List<User>>(user, HttpStatus.OK);
	}

	@PutMapping("/user/{userName}")
	public ResponseEntity<?> updateUser(@PathVariable("userName") String username, @RequestBody User user) {
		User curUser = userService.findByUserName(username);
		
		if (userService.isUserExist(user)) {
			throw new ResourceAlreadyExists(
					"Unable to update. A User with user name " + user.getUserName() + " already exist.");
		}
		

		curUser.setFirstName(user.getFirstName());
		curUser.setLastName(user.getLastName());
		curUser.setPassword(user.getPassword());
		//curUser.setAddress(user.getAddress());
		curUser.setMobile(user.getMobile());
		userService.updateUser(curUser);
		return new ResponseEntity<>(curUser, HttpStatus.OK);
	}

	@DeleteMapping("/user/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {

		Optional<User> user = userService.findByUserId(id);
		if (!user.isPresent()) {
			throw new ResourceNotFoundException("User not found with id = " + id);
		}

		userService.deleteUserById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	
	@GetMapping("/userdetail/{id}")
	public ResponseEntity<?> getUserDetailById(@PathVariable("id") Long id) {

		Optional<UserDetails> user = userService.findUserDetailById(id);

		return new ResponseEntity<>(user, HttpStatus.OK);
	
	}
	
	@PostMapping("/userdetail/save")
	public ResponseEntity<?> saveUserDetail(@RequestBody UserProfile userProfile) {
		System.out.println("inside createUser");

		userService.saveUserDetails(userProfile);
		HttpHeaders headers = new HttpHeaders();
		System.out.println("user saved");
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}
	
	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			System.out.println("authenticated");
		} catch (DisabledException e) {
			System.out.println("USER_DISABLED");
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			System.out.println("INVALID_CREDENTIALS");
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
