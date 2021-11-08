package ru.satird.pcs.services.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.satird.pcs.domains.ChatRoom;
import ru.satird.pcs.domains.Message;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.dto.chat.MessageDto;
import ru.satird.pcs.dto.chat.MessageStatus;
import ru.satird.pcs.dto.chat.NewMessageDto;
import ru.satird.pcs.mapper.MessageMapper;
import ru.satird.pcs.repositories.MessageRepository;
import ru.satird.pcs.services.AdService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChatRoomService chatRoomService;
    private final AdService adService;
    private final MessageMapper messageMapper;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, ChatRoomService chatRoomService,
                              AdService adService, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.chatRoomService = chatRoomService;
        this.adService = adService;
        this.messageMapper = messageMapper;
    }

    @Override
    public Message save(Message chatMessage) {
        chatMessage.setStatus(MessageStatus.RECEIVED);
        messageRepository.save(chatMessage);
        return chatMessage;
    }

    @Override
    public List<MessageDto> findChatMessages(User senderId, User recipientId) {
        Optional<String> chatId = chatRoomService.getChatId(senderId, recipientId, false);

        List<Message> messages =
                chatId.map(messageRepository::findByChatId).orElse(new ArrayList<>());

        messages.forEach(s -> {
            if (s.getRecipient().getId().equals(senderId.getId())) {
                s.setStatus(MessageStatus.DELIVERED);
                updateStatuses(s.getSender(), s.getRecipient(), s.getStatus());
            }
        });

        return messageMapper.mapMessageDtoList(messages.stream().sorted(Comparator.comparing(Message::getCreationDate)).collect(Collectors.toList()));
    }

    @Override
    public void updateStatuses(User senderId, User recipientId, MessageStatus status) {
        messageRepository.updateStatus(senderId.getId(), recipientId.getId(), status);
    }

    @Override
    public List<Message> findMessagesByChatId(String chatId) {
        return messageRepository.findByChatId(chatId);
    }

    @Override
    public Message createMessage(NewMessageDto messageDto, User currentUser, User recipient, String chatId) {
        Message message = new Message();
        message.setSender(currentUser);
        message.setRecipient(recipient);
        message.setChatId(chatId);
        message.setCreationDate(new Date());
        message.setText(messageDto.getText());
        return save(message);
    }

    @Override
    public MessageDto addMessage(Long adId, NewMessageDto messageDto, User currentUser, User recipient) {
        final Optional<String> chatId = chatRoomService.getChatId(currentUser, recipient, true);
        final List<ChatRoom> allChatByChatId = chatRoomService.findAllChatByChatId(chatId.orElseThrow(() -> new RuntimeException("Failed to create chat with chatId: " + chatId)));
        allChatByChatId.forEach(s -> s.setAd(adService.findAdById(adId)));
        final Message message = createMessage(messageDto, currentUser, recipient, chatId.orElseThrow(() -> new RuntimeException("Failed to create chat")));
        return messageMapper.mapMessageDto(message);
    }

    public List<MessageDto> getMessagesFromChat(String chatId, User currentUser) {
        List<Message> messages = findMessagesByChatId(chatId);
        final List<Message> messageList = messages.stream()
                .sorted(Comparator.comparing(Message::getCreationDate))
                .collect(Collectors.toList());
        messageList.forEach(s -> {
            if (s.getRecipient().getId().equals(currentUser.getId())) {
                s.setStatus(MessageStatus.DELIVERED);
                updateStatuses(s.getSender(), s.getRecipient(), s.getStatus());
            }
        });
        return messageMapper.mapMessageDtoList(messageList);
    }

    @Override
    public MessageDto createMessageAndConvertToDto(NewMessageDto messageDto, User currentUser, User recipient, String chatId) {
        final Message message = createMessage(messageDto, currentUser, recipient, chatId);
        return messageMapper.mapMessageDto(message);
    }
}
