package emlakburada.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import emlakburada.dto.UserRequest;
import emlakburada.dto.response.UserResponse;
import emlakburada.model.User;
import emlakburada.model.enums.UserType;
import emlakburada.repository.UserRepository;

@SpringBootTest
class UserServiceTest {

	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	@BeforeEach
	void setup() {

		//// @formatter:off
		Mockito
		.when(userRepository.findAll())
		.thenReturn(prepareMockUserList());
		
		// @formatter:on

	}

	private List<User> prepareMockUserList() {
		List<User> userList = new ArrayList<User>();
		userList.add(new User(UserType.INDIVIDUAL, "cem", "cem@patika.com"));
		userList.add(new User(UserType.INDIVIDUAL, "emre", "emre@patika.com"));
		return userList;
	}

	@Test
	void getAllUserTest() {

		List<UserResponse> allUser = userService.getAllUser();

		assertNotNull(allUser);

		assertThat(allUser.size()).isNotZero();
	}

	@Test
	void saveUserTest() {

		userService.saveUser(prepareUser());
		
		Mockito.verify(userRepository).save(any());

	}

	private UserRequest prepareUser() {
		return new UserRequest(UserType.INDIVIDUAL, "cem", "", null, null);
	}

}
