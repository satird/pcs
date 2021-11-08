package ru.satird.pcs.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommentVisibleDto {

    private String text;
    private UserDto author;
    private Date creationDate;
}
