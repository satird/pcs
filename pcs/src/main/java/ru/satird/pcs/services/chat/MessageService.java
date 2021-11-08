package ru.satird.pcs.services.chat;

import ru.satird.pcs.domains.Message;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.dto.chat.MessageDto;
import ru.satird.pcs.dto.chat.MessageStatus;
import ru.satird.pcs.dto.chat.NewMessageDto;

import java.util.List;

public interface MessageService {

    Message save(Message chatMessage);
    List<MessageDto> findChatMessages(User senderId, User recipientId);
    void updateStatuses(User senderId, User recipientId, MessageStatus status);
    List<Message> findMessagesByChatId(String chatId);
    Message createMessage(NewMessageDto messageDto, User currentUser, User recipient, String chatId);
    MessageDto addMessage(Long adId, NewMessageDto messageDto, User currentUser, User recipient);
    List<MessageDto> getMessagesFromChat(String chatId, User currentUser);

    MessageDto createMessageAndConvertToDto(NewMessageDto messageDto, User currentUser, User recipient, String chatId);
}
