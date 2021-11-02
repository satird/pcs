package ru.satird.pcs.services.chat;

import ru.satird.pcs.domains.Message;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.dto.chat.MessageDto;
import ru.satird.pcs.dto.chat.MessageStatus;

import java.util.List;

public interface MessageService {

    Message save(Message chatMessage);
    List<Message> findChatMessages(User senderId, User recipientId);
    void updateStatuses(User senderId, User recipientId, MessageStatus status);
    List<Message> findMessagesByChatId(String chatId);
    Message createMessage(MessageDto messageDto, User currentUser, User recipient, String chatId);
}
