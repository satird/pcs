package ru.satird.pcs.services.chat;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.satird.pcs.domains.Message;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.dto.chat.MessageDto;
import ru.satird.pcs.dto.chat.MessageStatus;
import ru.satird.pcs.repositories.MessageRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;
    private ChatRoomService chatRoomService;
    private ModelMapper modelMapper;

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setRepository(MessageRepository repository) {
        this.messageRepository = repository;
    }

    @Autowired
    public void setChatRoomService(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @Override
    public Message save(Message chatMessage) {
        chatMessage.setStatus(MessageStatus.RECEIVED);
        messageRepository.save(chatMessage);
        return chatMessage;
    }

    @Override
    public List<Message> findChatMessages(User senderId, User recipientId) {
        Optional<String> chatId = chatRoomService.getChatId(senderId, recipientId, false);

        List<Message> messages =
                chatId.map(cId -> messageRepository.findByChatId(cId)).orElse(new ArrayList<>());

        messages.forEach(s -> {
            if (s.getRecipient().getId().equals(senderId.getId())) {
                s.setStatus(MessageStatus.DELIVERED);
                updateStatuses(s.getSender(), s.getRecipient(), s.getStatus());
            }
        });

        return messages;
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
    public Message createMessage(MessageDto messageDto, User currentUser, User recipient, String chatId) {
        messageDto.setSender(currentUser);
        messageDto.setRecipient(recipient);
        messageDto.setChatId(chatId);
        messageDto.setCreationDate(new Date());
        return save(convertToMessageEntity(messageDto));
    }


    private Message convertToMessageEntity(MessageDto messageDto) {
        return modelMapper.map(messageDto, Message.class);
    }
}
