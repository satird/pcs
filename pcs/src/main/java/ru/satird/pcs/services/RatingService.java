package ru.satird.pcs.services;

import ru.satird.pcs.domains.User;

public interface RatingService {

    Float getRating(User user);
    void rate(User user, Float score);
}
