package ru.satird.pcs.restcontrollers;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.satird.pcs.domains.Ad;
import ru.satird.pcs.domains.Category;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.dto.AdDto;
import ru.satird.pcs.dto.UserDetailsImpl;
import ru.satird.pcs.repositories.specifications.AdSpecification;
import ru.satird.pcs.services.AdService;
import ru.satird.pcs.services.UserService;
import ru.satird.pcs.util.Views;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/rest/api")
public class AdRestController {

    private AdService adService;
    private ModelMapper modelMapper;
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setAdService(AdService adService) {
        this.adService = adService;
    }

    @GetMapping("/ad")
    @JsonView(Views.AdBasic.class)
    public ResponseEntity<List<Ad>> getAllAd(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "min", required = false) Float priceMin,
            @RequestParam(value = "max", required = false) Float priceMax,
            @RequestParam(value = "category", required = false) Category category,
            @RequestParam(value = "sort", required = false) String sort
    ) {
        logger.debug("getAllAd...");
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
        List<Ad> adList = adService.showAllAd(specification);
        List<Ad> collect;
        if (sort != null) {
            switch (sort) {
                case "date":
                    collect = adList.stream()
                            .sorted(Comparator.comparing(Ad::isPremium)
                                    .thenComparing(Comparator.comparing(Ad::getCreationDate).reversed())
                                    .thenComparing(ad -> ad.getAdvertiser().getRating()).reversed())
                            .collect(Collectors.toList());
                    break;
                case "price":
                    collect = adList.stream()
                            .sorted(Comparator.comparing(Ad::isPremium)
                                    .thenComparing(Comparator.comparing(Ad::getPrice).reversed())
                                    .thenComparing(ad -> ad.getAdvertiser().getRating()).reversed())
                            .collect(Collectors.toList());
                    break;
                case "title":
                    collect = adList.stream()
                            .sorted(Comparator.comparing(Ad::isPremium)
                                    .thenComparing(Comparator.comparing(Ad::getTitle).reversed())
                                    .thenComparing(ad -> ad.getAdvertiser().getRating()).reversed())
                            .collect(Collectors.toList());
                    break;
                default:
                    collect = adList.stream()
                            .sorted(Comparator.comparing(Ad::isPremium)
                                    .thenComparing(ad -> ad.getAdvertiser().getRating()).reversed())
                            .collect(Collectors.toList());
                    break;
            }
        } else {
            collect = adList.stream()
                    .sorted(Comparator.comparing(Ad::isPremium)
                            .thenComparing(ad -> ad.getAdvertiser().getRating()).reversed())
                    .collect(Collectors.toList());
        }
        return new ResponseEntity<>(collect, HttpStatus.OK);
    }

    @GetMapping("/ad/{id}")
    @JsonView(Views.AdBasic.class)
    public ResponseEntity<Ad> getAd(
            @PathVariable(value = "id") Long id
    ) {
        logger.debug("getAd...");
        Ad ad = adService.findAdById(id);
        return new ResponseEntity<>(ad, HttpStatus.OK);
    }

    @PostMapping("/ad")
    public ResponseEntity<Ad> createAd(
            @RequestBody AdDto ad
    ) {
        logger.debug("createAd...");
        final UserDetailsImpl userPrincipal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final User userById = userService.findUserById(userPrincipal.getId());
        final Ad newAd = adService.saveAd(convertToAdEntity(ad), userById);
        return new ResponseEntity<>(newAd, HttpStatus.CREATED);
    }

    @PutMapping("/ad/{id}")
    public ResponseEntity<Ad> updateAd(
            @PathVariable(value = "id") Long id,
            @RequestBody AdDto ad
    ) {
        logger.debug("updateAd...");
        adService.updateAd(id, convertToAdEntity(ad));
        return new ResponseEntity<>(adService.findAdById(id), HttpStatus.OK);
    }

    @DeleteMapping("/ad/{id}")
    public ResponseEntity<HttpStatus> deleteAd(
            @PathVariable(value = "id") Long id
    ) {
        logger.debug("deleteAd...");
        adService.deleteAd(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Ad convertToAdEntity(AdDto adDto) {
        return modelMapper.map(adDto, Ad.class);
    }
}
