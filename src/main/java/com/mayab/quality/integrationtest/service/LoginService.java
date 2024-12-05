package com.mayab.quality.integrationtest.service;

import com.mayab.quality.integrationtest.dao.IDAOUser;
import com.mayab.quality.integrationtest.model.User;

public class LoginService {
	IDAOUser dao;

	public LoginService(IDAOUser d) {
		dao = d;
	}
	
	public boolean login(String email, String pass) {
		
		User u = dao.findByUserName(email);
		if (u != null) {
			if (u.getPassword() == pass) {
				
				return true;
			}
			else {
				
				return false;
			}
		}
		else {
			return false;
		}
	}
}
