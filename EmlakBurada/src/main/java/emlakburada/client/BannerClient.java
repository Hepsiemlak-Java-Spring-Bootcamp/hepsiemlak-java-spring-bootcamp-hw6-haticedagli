package emlakburada.client;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import emlakburada.client.request.BannerRequest;
import emlakburada.client.response.BannerResponse;

@Service
public class BannerClient {

	private String bannerServiceUrl = "http://localhost:8081/banners";

	public void saveBanner(BannerRequest request) {
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<BannerResponse> response = restTemplate.postForEntity(bannerServiceUrl, request,
				BannerResponse.class);
		System.out.println(response.toString());

		if (response.getStatusCode() == HttpStatus.OK) {
			System.out.println(response.getBody().toString());
		} else {
			System.out.println("Banner Service Status Code: " + response.getStatusCode());
		}
	}

}
