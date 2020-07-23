package doan2020.SportTournamentSupportSystem.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import doan2020.SportTournamentSupportSystem.dtIn.RegisterDtIn;
import doan2020.SportTournamentSupportSystem.response.Response;
import doan2020.SportTournamentSupportSystem.service.impl.RegisterService;
import doan2020.SportTournamentSupportSystem.service.impl.VerificationTokenService;

@RestController
@RequestMapping("/register")
public class RegisterAPI {

	@Autowired
	RegisterService registerService;

	@Autowired
	VerificationTokenService verificationTokenService;

	/* ---------------- register NEW USER ------------------------ */
	@PostMapping
	public ResponseEntity<Response> createUser(@RequestBody RegisterDtIn user) {
		HttpStatus httpStatus = null;
		Response response = new Response();
		try {
			response = registerService.addNewUsers(user);
			
			if (response.getError().containsValue("001")) {	
				verificationTokenService.createVerification(user.getEmail(), user.getUsername());
				httpStatus = HttpStatus.OK;
			} else {
				httpStatus = HttpStatus.BAD_REQUEST;
			}
		} catch (Exception ex) {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Response>(response, httpStatus);
	}

}