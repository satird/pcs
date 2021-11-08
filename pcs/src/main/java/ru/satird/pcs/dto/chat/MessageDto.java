package ru.satird.pcs.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.satird.pcs.dto.UserDto;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {

    private Long id;
    private UserDto sender;
    private UserDto recipient;

    private String text;
    private Date creationDate;
    private String chatId;
    private MessageStatus status;
}
