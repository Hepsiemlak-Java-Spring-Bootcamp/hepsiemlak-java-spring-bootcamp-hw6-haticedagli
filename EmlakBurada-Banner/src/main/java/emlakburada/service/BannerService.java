package emlakburada.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import emlakburada.dto.request.BannerRequest;
import emlakburada.dto.response.BannerResponse;
import emlakburada.model.Banner;
import emlakburada.repository.BannerRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BannerService {

	@Autowired
	private BannerRepository repository;

	public List<BannerResponse> getAllBanners() {
		List<BannerResponse> bannerResponses = new ArrayList<>();
		for (Banner banner : repository.findAllBanners()) {
			bannerResponses.add(convertToBannerResponse(banner));
		}
		return bannerResponses;
	}

	public BannerResponse saveBanner(BannerRequest request) {
		Banner banner = repository.saveBanner(convertToBanner(request));
		log.info("banner saved: " + banner.toString());
		return convertToBannerResponse(banner);
	}

	private BannerResponse convertToBannerResponse(Banner banner) {
		BannerResponse response = new BannerResponse();
		response.setAdvertNo(banner.getAdvertNo());
		response.setPhone(banner.getPhone());
		response.setTotal(banner.getTotal());
		return response;
	}

	private Banner convertToBanner(BannerRequest request) {
		Banner banner = new Banner();
		banner.setAdvertNo(request.getAdvertNo());
		banner.setPhone(request.getPhone());
		banner.setTotal(request.getTotal());
		return banner;
	}

}
