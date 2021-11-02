package ru.satird.pcs.services.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.satird.pcs.domains.ChatRoom;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.repositories.ChatRoomRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    private ChatRoomRepository chatRoomRepository;

    @Autowired
    public void setChatRoomRepository(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    @Override
    public Optional<String> getChatId(User sender, User recipient, boolean createIfNotExist) {
        return chatRoomRepository
                .findBySenderAndRecipient(sender, recipient)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if(!createIfNotExist) {
                        return  Optional.empty();
                    }

                    String chatId = String.format("%s_%s", sender.getId(), recipient.getId());

                    ChatRoom senderRecipient = ChatRoom
                            .builder()
                            .chatId(chatId)
                            .sender(sender)
                            .recipient(recipient)
                            .build();

                    ChatRoom recipientSender = ChatRoom
                            .builder()
                            .chatId(chatId)
                            .sender(recipient)
                            .recipient(sender)
                            .build();
                    chatRoomRepository.save(senderRecipient);
                    chatRoomRepository.save(recipientSender);

                    return Optional.of(chatId);
                });
    }

    @Override
    public Set<ChatRoom> findAllChatCurrentUser(User currentUser) {
        return chatRoomRepository.findAllBySender(currentUser).orElseThrow(() -> new RuntimeException("Chat for this user: " + currentUser.getName() + " not found, please try again later"));
    }

    @Override
    public ChatRoom getChatByChatId(String chatId) {
        return chatRoomRepository.findFirstByChatId(chatId).orElseThrow(() -> new RuntimeException("Chat with chatId: " + chatId + " not found"));
    }

    @Override
    public List<ChatRoom> findAllChatByChatId(String chatId) {
        return chatRoomRepository.findAllByChatId(chatId).orElseThrow(() -> new RuntimeException("Chats with chatId: " + chatId + " not found"));
    }
}
