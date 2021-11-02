package ru.satird.pcs.services.chat;

import ru.satird.pcs.domains.ChatRoom;
import ru.satird.pcs.domains.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ChatRoomService {

    Optional<String> getChatId(User senderId, User recipientId, boolean createIfNotExist);

    Set<ChatRoom> findAllChatCurrentUser(User currentUser);

    ChatRoom getChatByChatId(String chatId);

    List<ChatRoom> findAllChatByChatId(String chatId);
}
