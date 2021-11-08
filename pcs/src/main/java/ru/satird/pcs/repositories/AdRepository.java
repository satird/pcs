package ru.satird.pcs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.satird.pcs.domains.Ad;

import java.util.Date;
import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long>, JpaSpecificationExecutor<Ad> {

    List<Ad> findAllByAdvertiser_Id(Long id);
    @Query("select a from Ad a where a.creationDate >= :sevenDaysAgoDate")
    List<Ad> findAllWithDateAfter(Date sevenDaysAgoDate);
}
