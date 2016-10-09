package com.dao;

import java.util.List;

import com.model.TblUser;

public interface TblUserDao {
	
	public void addUser(TblUser user);
	public void deleteUser(String pk);
	public void updateUser(TblUser user);
//	public TblUser getUser();
	List<TblUser> getAllUsers();

}
