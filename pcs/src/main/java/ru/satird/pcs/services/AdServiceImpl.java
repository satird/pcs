package ru.satird.pcs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.satird.pcs.domains.Ad;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.errors.AdsNotFoundException;
import ru.satird.pcs.repositories.AdRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdServiceImpl implements AdService {

    private AdRepository adRepository;

    @Autowired
    public void setAdRepository(AdRepository adRepository) {
        this.adRepository = adRepository;
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
    public Ad saveAd(Ad ad, User user) {
        ad.setAdvertiser(user);
        ad.setCreationDate(Timestamp.valueOf(LocalDateTime.now()));
        return adRepository.save(ad);
    }

    @Override
    public void updateAd(Long id, Ad ad) {
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
    public List<Ad> showAllAdByUserId(Long id) {
        return adRepository.findAllByAdvertiser_Id(id);
    }
}
