package ru.satird.pcs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.satird.pcs.domains.Ad;
import ru.satird.pcs.domains.Category;
import ru.satird.pcs.domains.Rating;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.dto.AdVisibleDto;
import ru.satird.pcs.errors.AdsNotFoundException;
import ru.satird.pcs.mapper.AdMapper;
import ru.satird.pcs.repositories.AdRepository;
import ru.satird.pcs.repositories.specifications.AdSpecification;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final AdMapper adMapper;

    @Autowired
    public AdServiceImpl(AdRepository adRepository, AdMapper adMapper) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
    }

    @Override
    public List<Ad> showAllAd(Specification<Ad> specification) {
        return adRepository.findAll(specification);
    }

    @Override
    public Ad findAdById(Long id) throws AdsNotFoundException {
        return adRepository.findById(id).orElseThrow(() -> new AdsNotFoundException("Private ad with id " + id + " not found"));
    }

    @Override
    public AdVisibleDto saveAdAndConvertAdVisibleDto(AdVisibleDto adVisibleDto, User user) {
        final Ad ad = adMapper.mapAd(adVisibleDto);
        ad.setAdvertiser(user);
        ad.setCreationDate(Timestamp.valueOf(LocalDateTime.now()));
        adRepository.save(ad);
        return adMapper.mapAdVisibleDto(ad);
    }

    @Override
    public void updateAd(Long id, AdVisibleDto ad) {
        try {
            final Ad changedAd = adRepository.findById(id).orElseThrow(() -> new Exception("Объявление с номером " + id + " не найдено"));
            changedAd.setTitle(ad.getTitle());
            changedAd.setText(ad.getText());
            changedAd.setPrice(ad.getPrice());
            changedAd.setCategory(ad.getCategory());
            changedAd.setPremium(ad.isPremium());
            adRepository.save(changedAd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAd(Long id) {
        adRepository.deleteById(id);
    }

    @Override
    public List<AdVisibleDto> showAllAdByUserId(Long id) {
        return adMapper.mapAdVisibleDtoList(adRepository.findAllByAdvertiser_Id(id));
    }

    @Override
    public List<Ad> getLastSevenDays() {
        LocalDate sevenDaysAgoDate = LocalDate.now().minusDays(7);
        Date weeklyAgo = Date.from(sevenDaysAgoDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return adRepository.findAllWithDateAfter(weeklyAgo);
    }

    public List<AdVisibleDto> getAds(String title, String text, Float priceMin, Float priceMax, Category category, String sort) {
        Specification<Ad> specification = Specification.where(null);
        if (title != null) {
            specification = specification.and(AdSpecification.titleLike(title));
        }
        if (text != null) {
            specification = specification.and(AdSpecification.textLike(text));
        }
        if (priceMin != null) {
            specification = specification.and(AdSpecification.priceGreaterThanOrEqual(priceMin));
        }
        if (priceMax != null) {
            specification = specification.and(AdSpecification.priceLesserThanOrEqual(priceMax));
        }
        if (category != null) {
            specification = specification.and(AdSpecification.belongsToCategory(category));
        }
        List<Ad> adList = showAllAd(specification);
        List<Ad> collect;
        if (sort != null) {
            switch (sort) {
                case "date":
                    collect = adList.stream()
                            .sorted(Comparator.comparing(Ad::isPremium)
                                    .thenComparing(Comparator.comparing(Ad::getCreationDate).reversed())
                                    .thenComparing(ad -> ad.getAdvertiser().getRating().stream()
                                            .mapToDouble(Rating::getScore)
                                            .average()
                                            .orElse(Double.NaN)).reversed())
                            .collect(Collectors.toList());
                    break;
                case "price":
                    collect = adList.stream()
                            .sorted(Comparator.comparing(Ad::isPremium)
                                    .thenComparing(Comparator.comparing(Ad::getPrice).reversed())
                                    .thenComparing(ad -> ad.getAdvertiser().getRating()
                                            .stream().mapToDouble(Rating::getScore).average().orElse(Double.NaN)).reversed())
                            .collect(Collectors.toList());
                    break;
                case "title":
                    collect = adList.stream()
                            .sorted(Comparator.comparing(Ad::isPremium)
                                    .thenComparing(Comparator.comparing(Ad::getTitle).reversed())
                                    .thenComparing(ad -> ad.getAdvertiser().getRating().stream().mapToDouble(Rating::getScore).average().orElse(Double.NaN)).reversed())
                            .collect(Collectors.toList());
                    break;
                default:
                    collect = adList.stream()
                            .sorted(Comparator.comparing(Ad::isPremium)
                                    .thenComparing(ad -> ad.getAdvertiser().getRating().stream().mapToDouble(Rating::getScore).average().orElse(Double.NaN)).reversed())
                            .collect(Collectors.toList());
                    break;
            }
        } else {
            collect = adList.stream()
                    .sorted(Comparator.comparing(Ad::isPremium)
                            .thenComparing(ad -> ad.getAdvertiser().getRating().stream().mapToDouble(Rating::getScore).average().orElse(Double.NaN)).reversed())
                    .collect(Collectors.toList());
        }
        return adMapper.mapAdVisibleDtoList(collect);
    }
}
