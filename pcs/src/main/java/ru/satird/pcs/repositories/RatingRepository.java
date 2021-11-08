package ru.satird.pcs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.satird.pcs.domains.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("SELECT AVG(e.score) FROM Rating e WHERE e.user.id = :userId")
    Float findAverageRatingByUser(Long userId);
}
