package emlakburada.service;

import emlakburada.dto.AuthRequest;
import emlakburada.dto.AuthResponse;
import emlakburada.entity.User;
import emlakburada.exception.UserNotFoundException;
import emlakburada.exception.UserPasswordNotValidException;
import emlakburada.repository.AuthRepository;
import emlakburada.util.JwtUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private AuthRepository authRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Test
    public void getToken_should_throw_exception_when_given_email_not_exist() {
        //given
        AuthRequest authRequest = new AuthRequest("haticeetoglu03@gmail.com","123456");

        BDDMockito
                .given(authRepository.findByEmail(authRequest.getEmail()))
                .willReturn(Optional.empty());

        //when
        Throwable throwable = catchThrowable(() -> authService.getToken(authRequest));

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(UserNotFoundException.class);
        UserNotFoundException exception = (UserNotFoundException) throwable;
        assertThat(exception.getMessage()).isEqualTo("User not found");
    }

    @Test
    public void getToken_should_throw_exception_when_given_password_and_user_password_not_matched() {
        //given
        AuthRequest authRequest = new AuthRequest("haticeetoglu03@gmail.com","123456");

        User user = new User();
        user.setEmail(authRequest.getEmail());
        user.setPassword("1234567");

        BDDMockito
                .given(authRepository.findByEmail(authRequest.getEmail()))
                .willReturn(Optional.of(user));

        //when
        Throwable throwable = catchThrowable(() -> authService.getToken(authRequest));

        //then
        assertThat(throwable)
                .isNotNull()
                .isInstanceOf(UserPasswordNotValidException.class);
        UserPasswordNotValidException exception = (UserPasswordNotValidException) throwable;
        assertThat(exception.getMessage()).isEqualTo("User's password not valid");
    }

    @Test
    public void getToken_should_return_token_with_correct_mapping() {
        //given
        AuthRequest authRequest = new AuthRequest("haticeetoglu03@gmail.com","123456");

        User user = new User();
        user.setEmail(authRequest.getEmail());
        user.setPassword("123456");

        BDDMockito
                .given(authRepository.findByEmail(authRequest.getEmail()))
                .willReturn(Optional.of(user));

        BDDMockito
                .given(jwtUtil.generateToken(user))
                .willReturn("token");

        //when
        AuthResponse authResponse = authService.getToken(authRequest);

        //then
        assertThat(authResponse).isNotNull();
        assertThat(authResponse.getToken()).isEqualTo("token");
    }

}