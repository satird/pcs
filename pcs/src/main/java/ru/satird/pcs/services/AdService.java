package ru.satird.pcs.services;

import org.springframework.data.jpa.domain.Specification;
import ru.satird.pcs.domains.Ad;
import ru.satird.pcs.domains.User;

import java.util.List;

public interface AdService {

    List<Ad> showAllAd(Specification<Ad> specification);
    Ad findAdById(Long id);
    Ad saveAd(Ad ad, User user);
    void updateAd(Long id, Ad ad);
    void deleteAd(Long id);
    List<Ad> showAllAdByUserId(Long id);
}
