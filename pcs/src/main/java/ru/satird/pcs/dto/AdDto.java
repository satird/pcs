package ru.satird.pcs.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.satird.pcs.domains.Category;
import ru.satird.pcs.domains.User;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class AdDto {

    private Long id;
    private String title;
    private String text;
    private Float price;
    private boolean premium;
    private User advertiser;
    private Category category;
    private Date creationDate;
}
