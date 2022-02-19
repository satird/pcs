package ru.satird.pcs.dto;

import lombok.Getter;
import lombok.Setter;
import ru.satird.pcs.domains.Category;

@Getter
@Setter
public class AdVisibleDto {

    private Long id;
    private String title;
    private String text;
    private Float price;
    private boolean premium;
    private UserDto advertiser;
    private Category category;
}
