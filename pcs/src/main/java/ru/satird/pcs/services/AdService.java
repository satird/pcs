package ru.satird.pcs.services;

import org.springframework.data.jpa.domain.Specification;
import ru.satird.pcs.domains.Ad;
import ru.satird.pcs.domains.Category;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.dto.AdVisibleDto;

import java.util.List;

public interface AdService {

    List<Ad> showAllAd(Specification<Ad> specification);
    Ad findAdById(Long id);
    AdVisibleDto saveAdAndConvertAdVisibleDto(AdVisibleDto adVisibleDto, User user);
    void updateAd(Long id, AdVisibleDto adVisibleDto);
    void deleteAd(Long id);
    List<AdVisibleDto> showAllAdByUserId(Long id);
    List<Ad> getLastSevenDays();
    List<AdVisibleDto> getAds(String title, String text, Float priceMin, Float priceMax, Category category, String sort);
}
