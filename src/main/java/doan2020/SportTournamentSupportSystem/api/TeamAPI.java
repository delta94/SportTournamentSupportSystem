package doan2020.SportTournamentSupportSystem.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import doan2020.SportTournamentSupportSystem.converter.TeamConverter;
import doan2020.SportTournamentSupportSystem.dtOut.TeamDtOut;
import doan2020.SportTournamentSupportSystem.entity.TeamEntity;
import doan2020.SportTournamentSupportSystem.response.Response;
import doan2020.SportTournamentSupportSystem.service.ITeamService;


@RestController
@RequestMapping("/teams")
public class TeamAPI {
	@Autowired
	private ITeamService service;

	@Autowired
	private TeamConverter converter;

	/*
	 * Tim kiem team theo id
	 */
	@GetMapping
	public ResponseEntity<Response> getTeam(@RequestParam(value = "id", required = true) Long id) {
		System.out.println("getTeam");
		HttpStatus httpStatus = HttpStatus.OK;
		Response response = new Response();
		Map<String, Object> config = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> error = new HashMap<String, Object>();
//		System.out.println("2");

		if (id == null) {
			result.put("team", null);
			config.put("global", 0);
			error.put("messageCode", 1);
			error.put("message", "Required team id");
			httpStatus = HttpStatus.OK;
			response.setConfig(config);
			response.setResult(result);
			response.setError(error);
			return new ResponseEntity<Response>(response, httpStatus);
		}

		TeamEntity res;


		res = service.findById(id);

		try {
			TeamDtOut resDTO = converter.toDTO(res);

			result.put("team", resDTO);
			config.put("global", 0);
			error.put("messageCode", 0);
			error.put("message", "Found");

		} catch (Exception e) {
			result.put("team", null);
			config.put("global", 0);
			error.put("messageCode", 1);
			error.put("message", "Tournament is not exist");
		}

		response.setConfig(config);
		response.setResult(result);
		response.setError(error);

		return new ResponseEntity<Response>(response, httpStatus);
	}

	/*
	 * Tao moi mot Tournament
	 * 
	 */
	@PostMapping
	@CrossOrigin
	public ResponseEntity<Response> createTeam(@RequestBody Map<String, Object> newTeam) {
		System.out.println("createTeam");
		HttpStatus httpStatus = HttpStatus.OK;
		Response response = new Response();
		Map<String, Object> config = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> error = new HashMap<String, Object>();
		try {
			TeamEntity teamEntity = converter.toEntity(newTeam);
			System.out.println("convert OK");
			service.addOne(teamEntity);
			System.out.println("add OK");
			TeamDtOut dto = converter.toDTO(teamEntity);
			System.out.println("convert OK");
			result.put("team", dto);
			config.put("global", 0);
			error.put("messageCode", 0);
			error.put("message", "Team create successfuly");
		} catch (Exception e) {
			result.put("team", null);
			config.put("global", 0);
			error.put("messageCode", 1);
			error.put("message", "Team create fail");
		}

		response.setConfig(config);
		response.setResult(result);
		response.setError(error);
		return new ResponseEntity<Response>(response, httpStatus);
	}

	/*
	 * Edit mot Team
	 * 
	 */
	@PutMapping
	@CrossOrigin
	public ResponseEntity<Response> editTeam(@RequestBody Map<String, Object> team, @RequestParam Long id) {
		System.out.println("editTeam");
		HttpStatus httpStatus = HttpStatus.OK;
		Response response = new Response();
		Map<String, Object> config = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> error = new HashMap<String, Object>();
		try {
			TeamEntity teamEntity = converter.toEntity(team);
			System.out.println("convert OK");
			TeamEntity newTeam = service.update(id, teamEntity);
			System.out.println("add OK");
			TeamDtOut dto = converter.toDTO(newTeam);
			System.out.println("convert OK");
			result.put("team", dto);
			config.put("global", 0);
			error.put("messageCode", 0);
			error.put("message", "Team update successfuly");
		} catch (Exception e) {
			result.put("tournament", null);
			config.put("global", 0);
			error.put("messageCode", 1);
			error.put("message", "Team update fail");
		}

		response.setConfig(config);
		response.setResult(result);
		response.setError(error);
		return new ResponseEntity<Response>(response, httpStatus);
	}
}
