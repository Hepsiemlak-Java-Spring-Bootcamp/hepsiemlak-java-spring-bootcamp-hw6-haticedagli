package emlakburada.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import emlakburada.client.BannerClientFeign;
import emlakburada.client.response.BannerResponse;
import emlakburada.dto.AdvertRequest;
import emlakburada.dto.response.AdvertResponse;
import emlakburada.exception.UserNotFoundException;
import emlakburada.model.Advert;
import emlakburada.model.User;
import emlakburada.model.enums.UserType;
import emlakburada.queue.QueueService;
import emlakburada.repository.AdvertRepository;
import emlakburada.repository.UserRepository;

@SpringBootTest
class AdvertServiceTest {

	@InjectMocks
	private AdvertService advertService;

	// --

	@Mock
	private AdvertRepository advertRepository;

	@Mock
	private BannerClientFeign bannerClientFeign;

	@Mock
	private QueueService queueService;

	@Mock
	private UserRepository userRepository;

	@Test()
	@DisplayName("when user not found throw exception. "
			+ "method name can be: should_throw_exception_when_user_not_found")
	void saveAdvertWithoutUserTest() {

		assertThrows(Exception.class, () -> {
			advertService.saveAdvert(prepareAdvertRequest());
		}, "İlan kaydedilemedi");

		// assertEquals("İlan kaydedilemedi", thrown.getMessage());

	}

	@Test
	void saveAdvertWithUserTest() throws Exception {
		AdvertRequest request = prepareAdvertRequest();

		Optional<User> user = Optional.of(prepareUser());
		//// @formatter:off
		Mockito
		.when(userRepository.findById(request.getUserId()))
		.thenReturn(user);
		
		Mockito
		.when(advertRepository.save(any()))
		.thenReturn(prepareAdvert("başlık"));
		
		Mockito
		.when(bannerClientFeign.saveBanner(any()))
		.thenReturn(new ResponseEntity<BannerResponse>(new BannerResponse(), HttpStatus.OK));
		
		// @formatter:on

		assertDoesNotThrow(() -> {
			AdvertResponse response = advertService.saveAdvert(request);
			assertEquals("başlık", response.getBaslik());
			verify(queueService).sendMessage(any());
			verify(bannerClientFeign).saveBanner(any());

		});

	}

	@Test
	void saveAdvertWithUserTest1() throws Exception {
		AdvertRequest request = prepareAdvertRequest();

		Optional<User> user = Optional.of(prepareUser());
		//// @formatter:off
		Mockito
		.when(userRepository.findById(request.getUserId()))
		.thenReturn(user);
		
		Mockito
		.when(advertRepository.save(any()))
		.thenReturn(prepareAdvert(""));
		
		Mockito
		.when(bannerClientFeign.saveBanner(any()))
		.thenReturn(new ResponseEntity<BannerResponse>(new BannerResponse(), HttpStatus.OK));
		
		// @formatter:on

		AdvertResponse response = advertService.saveAdvert(request);

		assertEquals("başlık", response.getBaslik());
		verify(queueService).sendMessage(any());
		verify(bannerClientFeign).saveBanner(any());

	}

	@Test
	void getAllAdvertsTest() {
		// when
		// given
		// verify

		Mockito.when(advertRepository.findAll()).thenReturn(prepareAdvertList());

		List<AdvertResponse> responseList = advertService.getAllAdverts();

		assertNotEquals(0, responseList.size());

		assertThat(responseList.size()).isNotZero();

		for (AdvertResponse response : responseList) {
			assertThat(response.getAdvertNo()).isEqualTo(0);

			assertEquals(new BigDecimal(12345), response.getFiyat());

		}

	}

	private List<Advert> prepareAdvertList() {
		List<Advert> adverts = new ArrayList<Advert>();
		adverts.add(prepareAdvert("başlık1"));
		adverts.add(prepareAdvert("başlık2"));
		adverts.add(prepareAdvert("başlık3"));
		return adverts;
	}

	private Advert prepareAdvert(String baslik) {
		Advert advert = new Advert();
		advert.setAdvertNo(0);
		advert.setBaslik(baslik);
		advert.setFiyat(new BigDecimal(12345));
		return advert;
	}

	private User prepareUser() {
		User user = new User(UserType.CORPORATE, "mock name", "email");
		return user;
	}

	private AdvertRequest prepareAdvertRequest() {
		AdvertRequest request = new AdvertRequest();
		request.setUserId(5);
		request.setBaslik("başlık");
		request.setSuresi(3);
		request.setFiyat(new BigDecimal(12345));
		return request;
	}

	@Test
	void getAdvertByAdvertIdTest() {

		// when
		// given
		// verify

		//// @formatter:off

		Mockito
		.when(advertRepository.findById(0))
		.thenReturn(Optional.of(prepareAdvert("başlık")));
		// @formatter:on

		AdvertResponse response = advertService.getAdvertByAdvertId(0);
		assertNotNull(response);
		assertEquals("başlık", response.getBaslik());
		assertNotNull(response.getKullanici());

	}

	@Test
	@DisplayName("should throw UserNotFoundException.class")
	void getAllFavoriteAdvertsByUserIdNotFoundUserTest() {

		assertThrows(UserNotFoundException.class, () -> {
			List<AdvertResponse> response = advertService.getAllFavoriteAdvertsByUserId(1);
			assertNull(response);
		}, "User Not Found");

	}

	@Test
	@DisplayName("should return List<AdvertResponse>")
	void getAllFavoriteAdvertsByUserIdTest() {

		User user = prepareUser();
		user.getFavoriIlanlar().add(prepareAdvert("başlık"));

		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));

		assertDoesNotThrow(() -> {
			List<AdvertResponse> response = advertService.getAllFavoriteAdvertsByUserId(1);

			assertNotNull(user.getFavoriIlanlar());

			assertThat(user.getFavoriIlanlar().size()).isNotZero();

			assertNotNull(response);

			assertThat(response.size()).isNotZero();
		});

	}

}
