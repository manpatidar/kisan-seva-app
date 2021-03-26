package com.manohar.kisansevapp.model;

public class UserProfile {
	
	private User user;
	private UserDetails userDetails;
	
	public UserProfile() {}
	
	public UserProfile(User user, UserDetails userDetails) {
		super();
		this.user = user;
		this.userDetails = userDetails;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}
	
	

}
