package ru.satird.pcs.restcontrollers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.satird.pcs.domains.Ad;
import ru.satird.pcs.domains.Category;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.dto.AdVisibleDto;
import ru.satird.pcs.dto.UserDetailsImpl;
import ru.satird.pcs.dto.payload.response.MessageResponse;
import ru.satird.pcs.mapper.AdMapper;
import ru.satird.pcs.services.AdService;
import ru.satird.pcs.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

@Api(description = "Контроллер объявлений")
@Slf4j
@RestController
@RequestMapping("/rest/api")
public class AdRestController {

    private final AdService adService;
    private final UserService userService;
    private final AdMapper adMapper;
    private final MessageSource messageSource;

    @Autowired
    public AdRestController(AdService adService, UserService userService, AdMapper adMapper, MessageSource messageSource) {
        this.adService = adService;
        this.userService = userService;
        this.adMapper = adMapper;
        this.messageSource = messageSource;
    }


    @ApiOperation(value = "Получить все объявления", notes = "Получить все существующие объявления")
    @ApiImplicitParams ({
            @ApiImplicitParam(name = "title", value = "Поиск по названию объявления", required = false, dataTypeClass = String.class, paramType = "query", example = "велосипед"),
            @ApiImplicitParam(name = "text", value = "Поиск по тексту объявления", required = false, dataTypeClass = String.class, paramType = "query", example = "замечательном состоянии"),
            @ApiImplicitParam(name = "min", value = "Найти объявление стоимостью не ниже указанной", required = false, dataTypeClass = Float.class, paramType = "query", example = "1"),
            @ApiImplicitParam(name = "max", value = "Найти объявление стоимостью не выше указанной", required = false, dataTypeClass = Float.class, paramType = "query", example = "100"),
            @ApiImplicitParam(name = "category", value = "Поиск по категории", required = false, dataType = "ru.satird.pcs.domains.Category", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "Сортировать. Допустимые значения: date, price, title", required = false, dataTypeClass = String.class, paramType = "query", example = "price")
    })
    @GetMapping("/ad")
    public ResponseEntity<List<AdVisibleDto>> getAllAd(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "min", required = false) Float priceMin,
            @RequestParam(value = "max", required = false) Float priceMax,
            @RequestParam(value = "category", required = false) Category category,
            @RequestParam(value = "sort", required = false) String sort
    ) {
        log.debug("getAllAd...");
        List<AdVisibleDto> collect = adService.getAds(title, text, priceMin, priceMax, category, sort);
        return new ResponseEntity<>(collect, HttpStatus.OK);
    }

    @ApiOperation(value = "Получить объявление по id", notes = "Получить объявление по индификатору id")
    @ApiImplicitParam (name = "id", value = "id объявления", required = true, dataTypeClass = Long.class, paramType = "path", example = "1")
    @GetMapping("/ad/{id}")
    public ResponseEntity<AdVisibleDto> getAd(
            @PathVariable(value = "id") Long id
    ) {
        log.debug("getAd...");
        Ad ad = adService.findAdById(id);
        final AdVisibleDto adVisibleDto = adMapper.mapAdVisibleDto(ad);
        return new ResponseEntity<>(adVisibleDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Создать объявление", notes = "Создать новое объявление")
    @ApiImplicitParam (name = "AdVisibleDto", value = "подробный пользовательский объект объявления", required = true, dataTypeClass = AdVisibleDto.class, dataType = "ru.satird.pcs.dto.AdVisibleDto", paramType = "body")
    @PostMapping("/ad")
    public ResponseEntity<AdVisibleDto> createAd(
            @RequestBody AdVisibleDto adVisibleDto
    ) {
        log.debug("createAd...");
        final UserDetailsImpl userPrincipal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final User userById = userService.findUserById(userPrincipal.getId());
        final AdVisibleDto newAd = adService.saveAdAndConvertAdVisibleDto(adVisibleDto, userById);
        return new ResponseEntity<>(newAd, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Изменить объявление", notes = "Изменить выбранное объявление")
    @ApiImplicitParams ({
            @ApiImplicitParam(name = "id", value = "id объявления", required = true, dataTypeClass = Long.class, paramType = "path", example = "3"),
            @ApiImplicitParam (name = "AdVisibleDto", value = "подробный пользовательский объект объявления", required = true, dataType = "ru.satird.pcs.dto.AdVisibleDto", paramType = "body")
    })
    @PutMapping("/ad/{id}")
    public ResponseEntity<MessageResponse> updateAd(
            HttpServletRequest request,
            @PathVariable(value = "id") Long id,
            @RequestBody AdVisibleDto adVisibleDto
    ) {
        log.debug("updateAd...");
        Locale locale = request.getLocale();
        final UserDetailsImpl userPrincipal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final User currentUser = userService.findUserById(userPrincipal.getId());
        final User byUsername = userService.findByUsername(adVisibleDto.getAdvertiser().getName());
        if (currentUser.equals(byUsername)) {
            adService.updateAd(id, adVisibleDto);
            return new ResponseEntity<>(new MessageResponse(messageSource.getMessage("message.ad.updateSuc", null, locale)), HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageResponse(messageSource.getMessage("message.ad.updateErr", null, locale)), HttpStatus.FORBIDDEN);
    }

    @ApiOperation(value = "Удалить объявление", notes = "Изменить объявление по id")
    @ApiImplicitParam(name = "id", value = "id объявления", required = true, dataTypeClass = Long.class, paramType = "path", example = "2")
    @DeleteMapping("/ad/{id}")
    public ResponseEntity<MessageResponse> deleteAd(
            HttpServletRequest request,
            @PathVariable(value = "id") Long id
    ) {
        log.debug("deleteAd...");
        Locale locale = request.getLocale();
        adService.deleteAd(id);
        return new ResponseEntity<>(new MessageResponse(messageSource.getMessage("message.ad.deleteSuc", null, locale)), HttpStatus.OK);
    }
}
