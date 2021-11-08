package ru.satird.pcs.dto.chat;

import lombok.Getter;
import lombok.Setter;
import ru.satird.pcs.dto.AdTitle;
import ru.satird.pcs.dto.UserDto;

@Getter
@Setter
public class ChatRoomDto {

    private String chatId;
    private UserDto sender;
    private UserDto recipient;
    private AdTitle ad;
}
