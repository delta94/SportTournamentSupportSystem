package doan2020.SportTournamentSupportSystem.testApi;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import doan2020.SportTournamentSupportSystem.api.LoginAPI;
import doan2020.SportTournamentSupportSystem.converter.UserConverter;
import doan2020.SportTournamentSupportSystem.dto.UserDTO;
import doan2020.SportTournamentSupportSystem.entity.UserEntity;
import doan2020.SportTournamentSupportSystem.repository.UserRepository;
import doan2020.SportTournamentSupportSystem.response.Response;
import doan2020.SportTournamentSupportSystem.service.IVerificationTokenService;
import doan2020.SportTournamentSupportSystem.service.impl.JwtService;
import doan2020.SportTournamentSupportSystem.service.impl.LoginService;
import doan2020.SportTournamentSupportSystem.service.impl.UserService;

@RunWith(SpringRunner.class)
public class TestLoginAPI {
	
	//Bean definition
	@TestConfiguration
	public static class testLoginAPIConfiguration {
		
		@Bean
		LoginService loginService() {
			return new LoginService();
		}
		
		@Bean
		BCryptPasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}

		@Bean
		JwtService jwtService() {
			return new JwtService();
		}
		
		@Bean
		UserService userService() {
			return new UserService();
		}

		@Bean
		UserConverter converter() {
			return new UserConverter();
		}
		
		@Bean
		LoginAPI loginApi() {
			return new LoginAPI();
		}
	}
	
	@MockBean
	LoginService loginService;
	
	@MockBean
	BCryptPasswordEncoder passwordEncoder;

	@MockBean
	JwtService jwtService;

	@MockBean
	UserRepository userRepository;
	
	@MockBean
	UserService userService;

	@MockBean
	UserConverter converter;

	@MockBean
	IVerificationTokenService verificationTokenService;
	
	@Autowired
	LoginAPI loginApi;
	
	UserDTO user4Dto;
	
	//Emulate before execute test
	@Before
	public void setUp() {
		//1
		Mockito.when(userService.findByUsername("user1")).thenReturn(null);
		//2
		UserEntity user2 = new UserEntity();
		user2.setId((long)2);
		user2.setStatus("unactive");
		Mockito.when(userService.findByUsername("user2")).thenReturn(user2);
		//3
		UserEntity user3 = new UserEntity();
		user3.setId((long)3);
		user3.setStatus("active");
		user3.setUsername("user3");
		user3.setPassword("user3");
		Mockito.when(userService.findByUsername("user3")).thenReturn(user3);
		//4
		UserEntity user4 = new UserEntity();
		user4.setId((long)4);
		user4.setStatus("active");
		user4.setUsername("user4");
		user4.setPassword("user4");
		Mockito.when(userService.findByUsername("user4")).thenReturn(user4);
		user4Dto = new UserDTO();
		Mockito.when(converter.toDTO(user4)).thenReturn(user4Dto);
		Mockito.when(jwtService.generateTokenLogin(user4)).thenReturn("user4Token");
	}
	
	@Test
	public void testLoginCaseUserNotExist() {
		//Test data
		UserDTO user1 = new UserDTO();
		user1.setUsername("user1");
		user1.setPassword("user1");
		
		//Get actual result
		ResponseEntity<Response> response = loginApi.login(user1);
		
		//Actual result
		int actualMessageCode = (int)response.getBody().getError().get("MessageCode");
		String actualMessage = (String)response.getBody().getError().get("Message");
		int actualConfigGlobal = (int)response.getBody().getConfig().get("Global");
		UserDTO actualUser = (UserDTO)response.getBody().getResult().get("User");
		String actualAuthentication = (String)response.getBody().getResult().get("Authentication");
		
		//Compare expected and actual
		Assert.assertEquals(1, actualMessageCode);
		Assert.assertEquals("User is not Exist", actualMessage);
		Assert.assertEquals(0, actualConfigGlobal);
		Assert.assertEquals(null, actualUser);
		Assert.assertEquals(null, actualAuthentication);
	}
	
	@Test
	public void testLoginCaseUserUnactive() {
		//Test data
		UserDTO user2 = new UserDTO();
		user2.setUsername("user2");
		user2.setPassword("user2");
		
		//Get actual result
		ResponseEntity<Response> response = loginApi.login(user2);
		
		//Actual result
		int actualMessageCode = (int)response.getBody().getError().get("MessageCode");
		String actualMessage = (String)response.getBody().getError().get("Message");
		int actualConfigGlobal = (int)response.getBody().getConfig().get("Global");
		UserDTO actualUser = (UserDTO)response.getBody().getResult().get("User");
		String actualAuthentication = (String)response.getBody().getResult().get("Authentication");
		
		//Compare expected and actual
		Assert.assertEquals(1, actualMessageCode);
		Assert.assertEquals("User is not active", actualMessage);
		Assert.assertEquals(0, actualConfigGlobal);
		Assert.assertEquals(null, actualUser);
		Assert.assertEquals(null, actualAuthentication);
	}
	
	@Test
	public void testLoginCaseIncorrectPassword() {
		//Test data
		UserDTO user3 = new UserDTO();
		user3.setUsername("user3");
		user3.setPassword("user321");
		
		//Get actual result
		ResponseEntity<Response> response = loginApi.login(user3);
		
		//Actual result
		int actualMessageCode = (int)response.getBody().getError().get("MessageCode");
		String actualMessage = (String)response.getBody().getError().get("Message");
		int actualConfigGlobal = (int)response.getBody().getConfig().get("Global");
		UserDTO actualUser = (UserDTO)response.getBody().getResult().get("User");
		String actualAuthentication = (String)response.getBody().getResult().get("Authentication");
		
		//Compare expected and actual
		Assert.assertEquals(1, actualMessageCode);
		Assert.assertEquals("Password wrong", actualMessage);
		Assert.assertEquals(0, actualConfigGlobal);
		Assert.assertEquals(null, actualUser);
		Assert.assertEquals(null, actualAuthentication);
	}
	
	@Test
	public void testLogin() {
		//Test data
		UserDTO user4 = new UserDTO();
		user4.setUsername("user4");
		user4.setPassword("user4");
		
		//Get actual result
		ResponseEntity<Response> response = loginApi.login(user4);
		
		//Actual result
		int actualMessageCode = (int)response.getBody().getError().get("MessageCode");
		String actualMessage = (String)response.getBody().getError().get("Message");
		int actualConfigGlobal = (int)response.getBody().getConfig().get("Global");
		UserDTO actualUser = (UserDTO)response.getBody().getResult().get("User");
		String actualAuthentication = (String)response.getBody().getResult().get("Authentication");
		
		//Compare expected and actual
		Assert.assertEquals(0, actualMessageCode);
		Assert.assertEquals("Login successfull", actualMessage);
		Assert.assertEquals(0, actualConfigGlobal);
		Assert.assertEquals(user4Dto, actualUser);
		Assert.assertEquals("user4Token", actualAuthentication);
	}
	
}