package ru.satird.pcs.services;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.satird.pcs.domains.Ad;
import ru.satird.pcs.domains.Category;
import ru.satird.pcs.domains.Comment;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.repositories.AdRepository;

import java.util.ArrayList;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
class AdServiceImplTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AdRepository repository;

    @MockBean
    private User user;

    @Test
    void should_find_no_ads_if_repository_is_empty() {
        Iterable<Ad> ads = repository.findAll();
        assertThat(ads).isEmpty();
    }

    @Test
    void should_store_an_ad() {
        Ad ad = repository.save(new Ad(1L, "test title", "test text", 0.5f, true, new User(), Category.BOOKS, new Date(), new ArrayList<Comment>()));

        assertThat(ad).hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("title", "test title")
                .hasFieldOrPropertyWithValue("text", "test text")
                .hasFieldOrPropertyWithValue("price", 0.5f)
                .hasFieldOrPropertyWithValue("premium", true)
                .hasFieldOrPropertyWithValue("category", Category.BOOKS);
    }

    @Test
    void should_find_all_ads() {
        Ad ad1 = new Ad();
        entityManager.persist(ad1);

        Ad ad2 = new Ad();
        entityManager.persist(ad2);

        Ad ad3 = new Ad();
        entityManager.persist(ad3);

        Iterable<Ad> ads = repository.findAll();

        assertThat(ads).hasSize(3).contains(ad1, ad2, ad3);
    }

    @Test
    void should_find_ad_by_id() {
        Ad ad1 = new Ad();
        entityManager.persist(ad1);

        Ad ad2 = new Ad();
        entityManager.persist(ad2);

        Ad foundAd = repository.findById(ad2.getId()).get();

        assertThat(foundAd).isEqualTo(ad2);
    }

    @Test
    void should_save_ad() {
        Ad ad = new Ad(null, "hello", "world", 4f, true, user, Category.BOOKS, null, null);
        repository.save(ad);

        Ad checkAd = repository.getById(ad.getId());
        assertThat(checkAd.getId()).isEqualTo(ad.getId());
        assertThat(checkAd.getTitle()).isEqualTo(ad.getTitle());
        assertThat(checkAd.getText()).isEqualTo(ad.getText());
        assertThat(checkAd.getPrice()).isEqualTo(ad.getPrice());
        assertThat(checkAd.isPremium()).isEqualTo(ad.isPremium());
        assertThat(checkAd.getCategory()).isEqualTo(ad.getCategory());
    }

    @Test
    void should_update_ad() {
        Ad ad1 = new Ad(null, "hello", "world", 4f, true, null, null, null, null);
        Ad ad2 = new Ad(null, "hello21", "world2", 8f, false, null, null, null, null);

        entityManager.persist(ad1);
        entityManager.persist(ad2);

        Ad updatedAd = new Ad(null, "update hello", "update world", 2f, true, null, null, null, null);

        Ad finalAd = repository.findById(ad2.getId()).get();
        finalAd.setTitle(updatedAd.getTitle());
        finalAd.setText(updatedAd.getText());
        finalAd.setPrice(updatedAd.getPrice());
        finalAd.setPremium(updatedAd.isPremium());

        repository.save(finalAd);

        Ad checkAd = repository.findById(ad2.getId()).get();
        assertThat(checkAd.getId()).isEqualTo(ad2.getId());
        assertThat(checkAd.getTitle()).isEqualTo(updatedAd.getTitle());
        assertThat(checkAd.getText()).isEqualTo(updatedAd.getText());
        assertThat(checkAd.getPrice()).isEqualTo(updatedAd.getPrice());
        assertThat(checkAd.isPremium()).isEqualTo(updatedAd.isPremium());
    }

    @Test
    void should_delete_ad() {
        Ad ad1 = new Ad(null, "hello", "world", 4f, true, null, null, null, null);
        Ad ad2 = new Ad(null, "hello21", "world2", 8f, false, null, null, null, null);
        Ad ad3 = new Ad(null, "hello3", "world3", 12f, false, null, null, null, null);
        entityManager.persist(ad1);
        entityManager.persist(ad2);
        entityManager.persist(ad3);
        repository.deleteById(ad2.getId());
        Iterable<Ad> ads = repository.findAll();
        assertThat(ads).hasSize(2).contains(ad1, ad3);
    }
}