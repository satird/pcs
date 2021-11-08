package ru.satird.pcs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.satird.pcs.domains.Rating;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.repositories.RatingRepository;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public Float getRating(User user) {
        return ratingRepository.findAverageRatingByUser(user.getId());
    }

    @Override
    public void rate(User user, Float score) {
        Rating rating = new Rating();
        rating.setUser(user);
        rating.setScore(score);
        ratingRepository.save(rating);
    }
}
