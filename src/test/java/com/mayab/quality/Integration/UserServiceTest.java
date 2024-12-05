package com.mayab.quality.Integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mayab.quality.integrationtest.dao.IDAOUser;
import com.mayab.quality.integrationtest.dao.UserMysqlDAO;
import com.mayab.quality.integrationtest.model.User;
import com.mayab.quality.integrationtest.service.UserService;


class UserServiceTest extends DBTestCase{

	UserMysqlDAO daoMySql;
	private IDAOUser dao;
	private UserService service;
	
	public UserServiceTest() {
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS,"com.mysql.cj.jdbc.Driver");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,"jdbc:mysql://localhost:3306/calidadSoftware2024");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME,"root");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD,"123456");	
	
	}
	
	@BeforeEach
	protected
	void setUp() throws Exception {
		// Initialize DAO
		dao = new UserMysqlDAO();
		service = new UserService(dao);
		daoMySql = new UserMysqlDAO();
		
		IDatabaseConnection connection = getConnection();
		try {
			DatabaseOperation.TRUNCATE_TABLE.execute(connection, getDataSet());
			DatabaseOperation.CLEAN_INSERT.execute(connection, getDataSet());
		
		} catch(Exception e) {
			fail("Error in setup: "+ e.getMessage());
		} finally {
			connection.close();
		}
	}
	
	protected IDataSet getDataSet() throws Exception {
		return new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/initDB.xml"));
	}

	// 1.1. createUser: HappyPath
	@Test
	public void happyPath_test() {
		//INITIALIZE
		String pass = "12345678911";
		String name = "name";
		String email = "user2@email.com";
		
		//EXCERCISE
		User newUser = service.createUser(name, email, pass);
		
		//VERIFICATION
		assertThat(newUser.getName(), is(name));
		assertThat(newUser.getEmail(), is(email));
		assertThat(newUser.getPassword(), is(pass));
	}
	
	// 1.2. createUser: email already
	@Test
	public void duplicatedEmail_test() {
		//INITIALIZE
		String email = "user@email.com";
		User user1 = new User("name", email, "12345678911");
		daoMySql.save(user1);
		
		//EXCERCISE
		User user2 = service.createUser("duplicado", email, "1234jcjhdfv");
		
		
		//VERIFICATION
		assertThat(user1.getName(), is(user2.getName()));
	}
	
	// 1.3. createUser: short password
	@Test
	public void whenPasswordShort_test() {
		//INITIALIZE
		String shortPass = "123";
		String name = "user";
		String email = "user@email.com";
		User user = null;
		
		//EXCERCISE
		User newUser = service.createUser(name, email, shortPass);
		
		//VERIFICATION
		assertThat(newUser, is(user));
	}
	
	
	// 2. updateUser
	@Test
	public void update_test() {
		//INITIALIZE
		User user = new User("name","user@email", "12345678911");
		user.setId(1);
		daoMySql.save(user);
		
		User newUser = new User("new", "user@email", "11111111111");
		newUser.setId(1);
		
		//EXCERCISE
		User updUser = service.updateUser(newUser);
		
		//VERIFICATION
		assertThat(updUser.getName(), is(newUser.getName()));
		assertThat(updUser.getEmail(), is(newUser.getEmail()));
		assertThat(updUser.getPassword(), is(newUser.getPassword()));
	}
	
	// 3. deleteUser
	@Test
	public void delete_test() {
		//INITIALIZE
		User user = new User("name","user@email", "12345678911");
		user.setId(1);
		daoMySql.save(user);
		
		User expected = null;
		
		//EXCERCISE
		boolean deleted = service.deleteUser(1);
		
		
		User found = service.findUserByEmail("user@email");
		
		//VERIFICATION
		assertThat(deleted, is(true));
		assertThat(found, is(expected));
		
	}
	
	// 4. findAllUsers
	@Test
	public void findAllUsers() {
		//INITIALIZE
		User u1 = new User("user1", "email1", "pass1");
		User u2 = new User("user2", "email2", "pass2");
		
		daoMySql.save(u1);
		daoMySql.save(u2);
		
		u1.setId(1);
		u2.setId(2);
		
		
		List<User> users = new ArrayList<User>();
		
		users.add(u1);
		users.add(u2);
		
		//EXCERCISE
		List<User> found = service.findAllUsers();
		
		//VERIFICATION
		for (int i = 0; i < users.size(); i++) {
		    assertEquals(users.get(i).getEmail(), found.get(i).getEmail());
		}
	}
	
	// 5. findUserByEmail
	@Test
	public void findUser_email() {
		//INITIALIZE
		User existingUser = new User("name","user@email", "12345678911");
		existingUser.setId(1);
		daoMySql.save(existingUser);
		
		//EXCERCISE
		User found = service.findUserByEmail("user@email");
		
		//VERIFICATION
		assertThat(found.getName(), is(existingUser.getName()));
		assertThat(found.getEmail(), is(existingUser.getEmail()));
		assertThat(found.getPassword(), is(existingUser.getPassword()));
		
	}
	
	// 6. findUserById
	@Test
	public void findUser_id() {
		//INITIALIZE
		User existingUser = new User("name","user@email", "12345678911");
		existingUser.setId(1);
		daoMySql.save(existingUser);
		
		//EXCERCISE
		User found = service.findUserById(1);
		
		//VERIFICATION
		assertThat(found.getName(), is(existingUser.getName()));
		assertThat(found.getEmail(), is(existingUser.getEmail()));
		assertThat(found.getPassword(), is(existingUser.getPassword()));
	}
	

}
