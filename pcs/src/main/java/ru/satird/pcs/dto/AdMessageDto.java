package ru.satird.pcs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.satird.pcs.domains.Category;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdMessageDto {

    private String title;
    private String text;
    private Float price;
    @JsonProperty("advertiser")
    private UserDto advertiser;
    private Category category;
}
