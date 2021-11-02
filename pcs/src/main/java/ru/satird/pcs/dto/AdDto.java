package ru.satird.pcs.dto;


import lombok.Getter;
import lombok.Setter;
import ru.satird.pcs.domains.Category;
import ru.satird.pcs.domains.User;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
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
