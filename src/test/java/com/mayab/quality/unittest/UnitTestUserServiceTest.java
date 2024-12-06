package com.mayab.quality.unittest;

//import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;

// Import hamcrest
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.mayab.quality.loginunittest.dao.IDAOUser;
import com.mayab.quality.loginunittest.model.User;
import com.mayab.quality.loginunittest.service.UserService;



class UnitTestUserServiceTest {
	
	private UserService service;
	private IDAOUser dao;
	private User user;
	private HashMap<Integer, User>db;
	
	@BeforeEach
	public void setUp() throws Exception{
		// We're just testing the UserService, not the dao.
		
		dao = mock(IDAOUser.class);
		service = new UserService(dao);
		db = new HashMap<Integer, User>();
		
	}
	@Test
	public void whenPasswordShort_test() {
		//INITIALIZE
		String shortPass = "123";
		String name = "user";
		String email = "user@email.com";
		User user = null;
		
		//Fake code for findUserByEmail & save method
		when(dao.findUserByEmail(anyString())).thenReturn(user);
		when(dao.save(any(User.class))).thenReturn(1);
		
		//EXCERCISE
		user = service.createUser(name, email, shortPass);
		User expected = null;
		
		//VERIFICATION
		User exp = new User("name", "email@email.com", "123456789");
		assertThat(user, is(exp));
	}
	
	@Test
	public void whenPasswordLong_test() {
		//INITIALIZE
		String longPass = "12345678911234567";
		String name = "user";
		String email = "user@email.com";
		User user = null;
		
		//Fake code for findUserByEmail
		when(dao.findUserByEmail(anyString())).thenReturn(user);
		
		//EXCERCISE
		user = service.createUser(name, email, longPass);
		
		//VERIFICATION
		assertThat(user, is(nullValue()));
	}
	
	@Test
	public void duplicatedEmail_test() {
		//INITIALIZE
		String pass1 = "12345678911";
		String name1 = "name";
		String email1 = "user2@email.com";
		
		User user2 = new User("duplicado", email1,pass1);
		
		//Fake code for findUserByEmail
		when(dao.findUserByEmail(email1)).thenReturn(user2);
		
		//EXCERCISE
		User newUser = service.createUser(name1, email1, pass1);
		
		//VERIFICATION
		assertThat(newUser,is(user2));
	}
	
	@Test
	public void happyPath_test() {
		//INITIALIZATION
		int sizeBefore = db.size();
		String pass = "12345678911";
		String name = "name";
		String email = "user2@email.com";
		
		//Fake code for findUserByEmail & save method
		when(dao.findUserByEmail(anyString())).thenReturn(null);
		when(dao.save(any(User.class))).thenReturn(1);
		
		//EXCERCISE
		User newUser = service.createUser(name, email, pass);
		
		//VERIFICATION
		assertThat(newUser, (notNullValue()));
		assertThat(newUser.getName(), is(name));       
		assertThat(newUser.getEmail(), is(email));
		assertThat(newUser.getPassword(), is(pass));
		
	}
	
	// Remember this is the test for service.update (no dao, the dao is a mock, that's why we are using a fake part)
	@Test
	public void update_test() {
		//INITIALIZE
		User oldUser = new User("oldUser","oldEmail", "oldPass");
		
		oldUser.setId(1);
		db.put(1,oldUser);
		
		
		User newUser = new User("newUser", "oldEmail", "newPass");
		newUser.setId(1);
		
		//Fake code
		when(dao.findById(1)).thenReturn(oldUser);
		
		when(dao.updateUser(any(User.class))).thenAnswer(new Answer<User>() {
			//Method within the class
			public User answer(InvocationOnMock invocation) throws Throwable{
				// Set the behavior in every invocation
				User arg = (User) invocation.getArguments()[0];
				db.replace(arg.getId(), arg);
				
				// Return the invoked value
				return db.get(arg.getId());
			}
		}
		);
		//EXCERCISE
		User result = service.updateUser(newUser);
		
		//VERIFICATION
		assertThat(result.getName(),is("newUser"));
		assertThat(result.getPassword(),is("newPass"));
	}
	
	@Test
	public void delete_test() {
		//INITIALIZE
		int id = 1;
		//Fake code
		when(dao.deleteById(anyInt())).thenReturn(true);
		
		//EXCERCISE
		boolean deleted = service.deleteUser(id);
		
		//VERIFICATION
		assertThat(deleted, is(true));
		User foundUser = service.findUserById(id);
	    assertThat(foundUser, is(nullValue()));
	}
	
	@Test
	public void findUser_happyPath() {
		//INITIALIZE
		User existingUser = new User("User Name", "user@email.com", "Pass");
		
		//fake code
		when(dao.findUserByEmail(anyString())).thenReturn(existingUser);
		
		//EXCERCISE
		User found = service.findUserByEmail("user@email.com");
		
		//VERIFICATION
		assertThat(found, is(existingUser));
	}
	
	@Test
	public void findUser_notFound() {
		//INITIALIZE
		String email = "user@email.com";
		
		//fake code
		when(dao.findUserByEmail(email)).thenReturn(null);
		
		//EXCERCISE
		User found = service.findUserByEmail(email);
		
		User none = null;
		
		//VERIFICATION
		assertThat(found, is(none));
				
	}
	
	@Test
	public void findAllUsers() {
		//INITIALIZE
		User u1 = new User("user1", "email1", "pass1");
		User u2 = new User("user2", "email2", "pass2");
		List<User> users = new ArrayList<User>();
		users.add(u1);
		users.add(u2);
		
		//fake code
		when(dao.findAll()).thenReturn(users);
		
		//EXCERCISE
		List<User> found = service.findAllUsers();
		
		//VERIFICATION
		assertThat(found, is(users));
	}
}