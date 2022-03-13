package emlakburada.service;

import emlakburada.dto.request.BannerRequest;
import emlakburada.dto.response.BannerResponse;
import emlakburada.model.Banner;
import emlakburada.repository.BannerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class BannerServiceTest {

    @Mock
    private BannerRepository bannerRepository;

    @InjectMocks
    private BannerService bannerService;

    @Test
    public void getAllBanners_should_return_all_banners_with_correct_mapping() {
        //given
        Banner banner = new Banner();
        banner.setAdvertNo(1);
        banner.setPhone("05555555555");
        banner.setTotal(1);

        BDDMockito
                .given(bannerRepository.findAllBanners())
                .willReturn(List.of(banner));

        //when
        List<BannerResponse> result = bannerService.getAllBanners();

        //then
        assertThat(result.size()).isEqualTo(1);

        BannerResponse bannerResponse = result.get(0);
        assertThat(bannerResponse.getAdvertNo()).isEqualTo(1);
        assertThat(bannerResponse.getPhone()).isEqualTo("05555555555");
        assertThat(bannerResponse.getTotal()).isEqualTo(1);
    }

    @Test
    public void saveBanner_should_save_and_return_with_correct_mapping() {
        //given
        BannerRequest bannerRequest = new BannerRequest();
        bannerRequest.setAdvertNo(1);
        bannerRequest.setPhone("05555555555");
        bannerRequest.setTotal(1);

        Banner banner = new Banner();
        banner.setAdvertNo(1);
        banner.setPhone("05555555555");
        banner.setTotal(1);

        BDDMockito
                .given(bannerRepository.saveBanner(banner))
                .willReturn(banner);

        //when
        BannerResponse result = bannerService.saveBanner(bannerRequest);

        //then
        assertThat(result).isNotNull();

        assertThat(result.getAdvertNo()).isEqualTo(1);
        assertThat(result.getPhone()).isEqualTo("05555555555");
        assertThat(result.getTotal()).isEqualTo(1);
    }

}