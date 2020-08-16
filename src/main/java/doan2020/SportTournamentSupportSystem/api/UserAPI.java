package doan2020.SportTournamentSupportSystem.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import doan2020.SportTournamentSupportSystem.converter.UserConverter;
import doan2020.SportTournamentSupportSystem.dto.UserDTO;
import doan2020.SportTournamentSupportSystem.entity.RoleEntity;
import doan2020.SportTournamentSupportSystem.entity.UserEntity;
import doan2020.SportTournamentSupportSystem.response.Response;
import doan2020.SportTournamentSupportSystem.service.IRoleService;
import doan2020.SportTournamentSupportSystem.service.IUserService;
import doan2020.SportTournamentSupportSystem.service.impl.FileStorageService;
import doan2020.SportTournamentSupportSystem.service.impl.VerificationTokenService;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserAPI {
	@Autowired
	private IUserService userService;

	@Autowired
	private IRoleService roleService;

	@Autowired
	private VerificationTokenService verificationTokenService;

	@Autowired
	private UserConverter userConverter;

	@Autowired
	private FileStorageService fileStorageService;

	/* get One User */

	@GetMapping("")
	public ResponseEntity<Response> getById(@RequestParam(value = "id") Long id) {
		System.out.println("UserAPI: getById: start");
		Response response = new Response();
		HttpStatus httpStatus = HttpStatus.OK;
		Map<String, Object> config = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> error = new HashMap<String, Object>();
		UserEntity user = new UserEntity();
		UserDTO dto = new UserDTO();

		try {
			if (id == null) {// id null
				result.put("User", null);
				config.put("Global", 0);
				error.put("MessageCode", 1);
				error.put("Message", "Required param id");
			} else {// id not null
				user = userService.findOneById(id);
				if (user == null) {// not found
					result.put("User", null);
					config.put("Global", 0);
					error.put("MessageCode", 1);
					error.put("Message", "Not found");
				} else {// found
					dto = userConverter.toDTO(user);
					result.put("User", dto);
					config.put("Global", 0);
					error.put("MessageCode", 0);
					error.put("Message", "Found");
				}
			}
			System.out.println("UserAPI: getById: no exception");

		} catch (Exception e) {
			System.out.println("UserAPI: getById: has exception");
			result.put("User", null);
			error.put("MessageCode", 1);
			error.put("Message", "Server error");
		}

		response.setError(error);
		response.setResult(result);
		response.setConfig(config);
		System.out.println("UserAPI: getById: finish");
		return new ResponseEntity<Response>(response, httpStatus);
	}

	@GetMapping("/getByUsername")
	public ResponseEntity<Response> getByUserName(@RequestParam(value = "username") String username) {
		System.out.println("UserAPI: getByUserName: start");
		Response response = new Response();
		HttpStatus httpStatus = HttpStatus.OK;
		Map<String, Object> config = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> error = new HashMap<String, Object>();
		UserEntity user = new UserEntity();
		UserDTO dto = new UserDTO();

		try {
			if (username == null) {// username null
				result.put("User", null);
				config.put("Global", 0);
				error.put("MessageCode", 1);
				error.put("Message", "Required param username");
			} else {// username not null
				user = userService.findByUsername(username);
				if (user == null) {// not found
					result.put("User", null);
					config.put("Global", 0);
					error.put("MessageCode", 1);
					error.put("Message", "Not found");
				} else {// found
					dto = userConverter.toDTO(user);
					result.put("User", dto);
					config.put("Global", 0);
					error.put("MessageCode", 0);
					error.put("Message", "Found");
				}
			}
			System.out.println("UserAPI: getByUserName: no exception");
		} catch (Exception e) {
			System.out.println("UserAPI: getByUserName: has exception");
			result.put("User", null);
			error.put("MessageCode", 1);
			error.put("Message", "Server error");
		}
		response.setError(error);
		response.setResult(result);
		response.setConfig(config);
		System.out.println("UserAPI: getByUserName: finish");
		return new ResponseEntity<Response>(response, httpStatus);
	}

	@GetMapping("/getByEmail")
	public ResponseEntity<Response> getByEmail(@RequestParam(value = "email") String email) {
		System.out.println("UserAPI: getByUserName: start");
		Response response = new Response();
		HttpStatus httpStatus = HttpStatus.OK;
		Map<String, Object> config = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> error = new HashMap<String, Object>();
		UserEntity user = new UserEntity();
		UserDTO dto = new UserDTO();
		try {
			if (email == null) {// email null
				result.put("User", null);
				config.put("Global", 0);
				error.put("MessageCode", 1);
				error.put("Message", "Required param email");
			} else {// email not null
				user = userService.findByEmail(email);
				if (user == null) {// not found
					result.put("User", null);
					config.put("Global", 0);
					error.put("MessageCode", 1);
					error.put("Message", "Not found");
				} else {// found
					dto = userConverter.toDTO(user);
					result.put("User", dto);
					config.put("Global", 0);
					error.put("MessageCode", 0);
					error.put("Message", "Found");
				}
			}
			System.out.println("UserAPI: getByUserName: no exception");
		} catch (Exception e) {
			System.out.println("UserAPI: getByUserName: has exception");
			result.put("User", null);
			error.put("MessageCode", 1);
			error.put("Message", "Server error");
		}
		response.setError(error);
		response.setResult(result);
		response.setConfig(config);
		System.out.println("UserAPI: getByUserName: finish");
		return new ResponseEntity<Response>(response, httpStatus);
	}

	/* ---------------- register NEW USER ------------------------ */
	@PostMapping
	public ResponseEntity<Response> createUser(@RequestBody UserDTO userDTO) {
		HttpStatus httpStatus = HttpStatus.OK;
		System.out.println("UserAPI: createUser: start");
		Response response = new Response();
		Map<String, Object> config = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> error = new HashMap<String, Object>();
		try {
			UserEntity newUser = userConverter.toEntity(userDTO);

			UserEntity userCheckUsername = userService.findByUsername(newUser.getUsername());
			UserEntity userCheckEmail = userService.findByEmail(newUser.getEmail());

			if (userCheckUsername != null || userCheckEmail != null) {// User exist
				error.put("MessageCode", 1);
				error.put("Message", "User is Exists");
			} else {

				RoleEntity roleEntity = roleService.findOneByName("ROLE_USER");
				if (roleEntity != null)
					newUser.setRole(roleEntity);
				else {
					roleEntity = roleService.findOneById((long) 1);
					newUser.setRole(roleEntity);
				}

				newUser.setStatus("deactive");

				newUser = userService.create(newUser);

				System.out.println("UserAPI: createUser: newUser: " + newUser);

				userDTO = userConverter.toDTO(newUser);

				verificationTokenService.createVerification(newUser.getEmail(), newUser.getUsername());

				result.put("User", userDTO);

				error.put("MessageCode", 0);
				error.put("Message", "Create user successful");
			}
			System.out.println("UserAPI: createUser: no exception");
		} catch (Exception e) {
			System.out.println("UserAPI: createUser: has exception");
			result.put("User", null);
			error.put("MessageCode", 1);
			error.put("Message", "Server error");
		}
		response.setError(error);
		response.setResult(result);
		response.setConfig(config);
		System.out.println("UserAPI: createUser: finish");
		return new ResponseEntity<Response>(response, httpStatus);
	}

	/* ---------------- Edit Profile User ------------------------ */
	@PutMapping("")
	public ResponseEntity<Response> editUser(@RequestParam(value = "id") Long id, @RequestBody UserDTO dto) {
		System.out.println("UserAPI: editUser: start");
		HttpStatus httpStatus = HttpStatus.OK;
		Response response = new Response();
		Map<String, Object> config = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> error = new HashMap<String, Object>();
		try {
			UserEntity userEntity = new UserEntity();
			if (id != null) {
				userEntity = userConverter.toEntity(dto);
				userEntity = userService.update(id, userEntity);

				result.put("User", userConverter.toDTO(userEntity));
				error.put("MessageCode", 0);
				error.put("Message", "Edit Profile User Successfull");
			} else {
				error.put("MessageCode", 1);
				error.put("Message", "required user id");
			}
			System.out.println("UserAPI: editUser: no exception");
		} catch (Exception ex) {
			System.out.println("UserAPI: editUser: has exception");
			result.put("User", null);
			error.put("MessageCode", 1);
			error.put("Message", "edit  User fail");
		}
		response.setError(error);
		response.setResult(result);
		response.setConfig(config);
		System.out.println("UserAPI: editUser: finish");
		return new ResponseEntity<Response>(response, httpStatus);
	}

	/* delete one User */
	@DeleteMapping("")
	public ResponseEntity<Response> deleteUser(@RequestParam(value = "id") Long id) {
		Response response = new Response();
		HttpStatus httpStatus = null;
		httpStatus = HttpStatus.OK;
		Map<String, Object> config = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> error = new HashMap<String, Object>();
		try {
			if (id != null) {
				userService.delete(id);
				error.put("MessageCode", 0);
				error.put("Message", "Delete User Successfull");
			} else {
				httpStatus = HttpStatus.NOT_FOUND;
				error.put("MessageCode", 1);
				error.put("Message", "required user id");
			}
		} catch (Exception e) {
			result.put("User", null);
			error.put("MessageCode", 1);
			error.put("Message", "delete  User fail");
		}
		response.setError(error);
		response.setResult(result);
		response.setConfig(config);
		return new ResponseEntity<Response>(response, httpStatus);
	}

	@PostMapping("/sendMail")
	public ResponseEntity<Response> sendMail(@RequestBody UserDTO userDTO) {
		Response response = new Response();
		HttpStatus httpStatus = null;
		Map<String, Object> config = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> error = new HashMap<String, Object>();
		httpStatus = HttpStatus.OK;
		try {
			if (verificationTokenService.createVerification(userDTO.getEmail(), userDTO.getUsername())) {
				error.put("MessageCode", 0);
				error.put("Message", "Sending mail successfully");

			} else {
				error.put("MessageCode", 1);
				error.put("Message", "Sending mail fail");
				response.setError(error);
				response.setResult(result);
				response.setConfig(config);
				return new ResponseEntity<Response>(response, httpStatus);
			}

		} catch (Exception e) {
			result.put("User", null);
			error.put("MessageCode", 1);
			error.put("Message", "send  Mail fail");
		}
		response.setError(error);
		response.setResult(result);
		response.setConfig(config);
		return new ResponseEntity<Response>(response, httpStatus);

	}

	@PostMapping("/forgotPassword")
	public ResponseEntity<Response> forgotPassword(@RequestBody UserDTO userDTO) {
		Response response = new Response();
		HttpStatus httpStatus = null;
		Map<String, Object> config = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> error = new HashMap<String, Object>();
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
		response.setError(error);
		response.setResult(result);
		response.setConfig(config);

		return new ResponseEntity<Response>(response, httpStatus);
	}

	@PutMapping("/uploadAvatar")
	public ResponseEntity<Response> uploadAvatar(@RequestParam("file") MultipartFile file,
			@RequestParam(value = "id") Long id) {
		Response response = new Response();
		HttpStatus httpStatus = HttpStatus.OK;
		Map<String, Object> config = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> error = new HashMap<String, Object>();
		try {
			if (id == null) {// id null
				result.put("User", null);
				config.put("Global", 0);
				error.put("MessageCode", 1);
				error.put("Message", "Required param id");
			} else {// id not null

				String fileName = fileStorageService.storeFile(file);
				System.out.println(fileName);
				if (fileName == null) {// fileName invalid
					result.put("User", null);
					config.put("Global", 0);
					error.put("MessageCode", 1);
					error.put("Message", "Could not store file");
				} else {// fileName valid
					System.out.println(fileName);
					UserDTO dto = new UserDTO();
					dto.setAvatar(fileName);
					UserEntity userEntity = userConverter.toEntity(dto);
					userEntity = userService.updateAvatar(id, userEntity);
					System.out.println(userEntity.getAvatar());

					result.put("User", userConverter.toDTO(userEntity));
					error.put("MessageCode", 0);
					error.put("Message", "Upload Avatar and Edit User Successfull");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		response.setError(error);
		response.setResult(result);
		response.setConfig(config);
		return new ResponseEntity<Response>(response, httpStatus);
	}
	
	@PutMapping("/uploadBackground")
	public ResponseEntity<Response> uploadBackground(
			@RequestParam("file") MultipartFile file,
			@RequestParam(value = "id") Long id) {
		
		System.out.println("UserAPI: uploadBackground: start");
		Response response = new Response();
		HttpStatus httpStatus = HttpStatus.OK;
		Map<String, Object> config = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> error = new HashMap<String, Object>();
		try {
			if (id == null) {// id null
				result.put("User", null);
				config.put("Global", 0);
				error.put("MessageCode", 1);
				error.put("Message", "Required param id");
			} else {// id not null

				String fileName = fileStorageService.storeFile(file);

				if (fileName == null) {// fileName invalid
					result.put("User", null);
					config.put("Global", 0);
					error.put("MessageCode", 1);
					error.put("Message", "Could not store file");
				} else {// fileName valid
					UserDTO dto = new UserDTO();
					dto.setBackground(fileName);
					UserEntity userEntity = userConverter.toEntity(dto);
					userEntity = userService.updateBackGround(id, userEntity);

					result.put("User", userConverter.toDTO(userEntity));
					error.put("MessageCode", 0);
					error.put("Message", "Upload background and Edit User Successfull");
				}
			}
			System.out.println("UserAPI: uploadBackground: no exception");
		} catch (Exception e) {
			System.out.println("UserAPI: uploadBackground: has exception");
		}
		response.setError(error);
		response.setResult(result);
		response.setConfig(config);
		System.out.println("UserAPI: uploadBackground: finish");
		return new ResponseEntity<Response>(response, httpStatus);
	}

}
