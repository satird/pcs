package ru.satird.pcs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.satird.pcs.domains.Ad;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long>, JpaSpecificationExecutor<Ad> {

    List<Ad> findAllByOrderByCreationDateAsc();
    List<Ad> findAllByAdvertiser_Id(Long id);
}
