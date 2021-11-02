package ru.satird.pcs.dto.chat;

import lombok.*;
import ru.satird.pcs.domains.User;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {

    private Long id;
    private User sender;
    private User recipient;

    private String text;
    private Date creationDate;
    private String chatId;
    private MessageStatus status;
}
