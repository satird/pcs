package ru.satird.pcs.mapper;

import org.mapstruct.Mapper;
import ru.satird.pcs.domains.Ad;
import ru.satird.pcs.dto.AdTitle;
import ru.satird.pcs.dto.AdVisibleDto;

import java.util.List;

@Mapper
public interface AdMapper {

    AdVisibleDto mapAdVisibleDto(Ad ad);
    Ad mapAd(AdVisibleDto adVisibleDto);
    List<Ad> mapAdList(List<AdVisibleDto> adVisibleDtoList);
    List<AdVisibleDto> mapAdVisibleDtoList(List<Ad> adList);
    AdTitle mapAdTitle(Ad ad);
}
