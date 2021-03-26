package com.manohar.kisansevapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manohar.kisansevapp.dao.UserDetailRepository;
import com.manohar.kisansevapp.dao.UserRepository;
import com.manohar.kisansevapp.model.User;
import com.manohar.kisansevapp.model.UserDetails;
import com.manohar.kisansevapp.model.UserProfile;
import com.manohar.kisansevapp.util.ResourceNotFoundException;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserDetailRepository userDetailRepository;
	
	@Override
	public void saveUser(User user) {
		userRepository.save(user);
	}

	@Override
	public Optional<User> findByUserId(Long id) {
		return userRepository.findById(id);
	}

	@Override
	public boolean isUserExist(User user) {
		Optional<User> u = userRepository.findUserByMobileUserName(user.getUserName(), user.getMobile());
		if(u.isPresent()) {
			return true;
		}
		return false;
	}

	@Override
	public Optional<User> findByMobile(Long mobile) {
		return userRepository.findByMobile(mobile);
	}

	@Override
	public List<User> findAllUser() {
		return userRepository.findAll();
	}

	@Override
	public void updateUser(User curUser) {
		saveUser(curUser);
	}

	@Override
	public void deleteUserById(long id) {
		userRepository.deleteById(id);
	}

	@Override
	public User findByUserName(String username) {
		Optional<User> curUser = userRepository.findByUsername(username);
		if (!curUser.isPresent()) {
			throw new ResourceNotFoundException("User not found with userName = " + username);
		} else {
		
			User u = curUser.get();
			u.setPassword("");
			return u;
		}
		
	}

	@Override
	public Optional<UserDetails> findUserDetailById(Long id) {
		return userDetailRepository.findUserDetailById(id);
	}

	@Override
	public void saveUserDetails(UserProfile userProfile) {
		User user = userProfile.getUser();
		UserDetails userDetails = userProfile.getUserDetails();
		
		userDetails.setUser(user);
		
		Optional<UserDetails> selectedUser = findUserDetailById(user.getId());
		if(selectedUser.isPresent()) {
			userDetailRepository.updateUserDetails(user.getId(), userDetails.getAddress(), userDetails.getCity(), userDetails.getPincode());
		} else {
			userDetailRepository.save(userDetails);
		}
		//updateUser(user);
	}

	
}
