package emlakburada.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import emlakburada.dto.UserRequest;
import emlakburada.dto.response.UserResponse;
import emlakburada.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/users")
	public ResponseEntity<?> saveUser(@RequestBody UserRequest request) {
		userService.saveUser(request);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping("/users")
	public ResponseEntity<List<UserResponse>> getAllUser() {
		return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
	}

}
