package com.manohar.kisansevapp.service;

import java.util.List;
import java.util.Optional;

import com.manohar.kisansevapp.model.User;
import com.manohar.kisansevapp.model.UserDetails;
import com.manohar.kisansevapp.model.UserProfile;

public interface UserService {
	
	public void saveUser(User user);
	
	public Optional<User> findByUserId(Long id);

	public boolean isUserExist(User user);

	public Optional<User> findByMobile(Long mobile);

	public List<User> findAllUser();

	public void updateUser(User curUser);

	public void deleteUserById(long id);

	public User findByUserName(String username);

	public Optional<UserDetails> findUserDetailById(Long id);

	public void saveUserDetails(UserProfile userProfile);
	
}
