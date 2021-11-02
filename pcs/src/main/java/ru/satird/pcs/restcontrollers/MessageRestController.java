package ru.satird.pcs.restcontrollers;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.apachecommons.CommonsLog;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.satird.pcs.domains.ChatRoom;
import ru.satird.pcs.domains.Message;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.dto.UserDetailsImpl;
import ru.satird.pcs.dto.chat.MessageDto;
import ru.satird.pcs.dto.chat.MessageStatus;
import ru.satird.pcs.services.AdService;
import ru.satird.pcs.services.UserService;
import ru.satird.pcs.services.chat.ChatRoomService;
import ru.satird.pcs.services.chat.MessageService;
import ru.satird.pcs.util.Views;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/rest/api")
public class MessageRestController {

    private MessageService messageService;
    private ChatRoomService chatRoomService;
    private UserService userService;
    private AdService adService;

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setAdService(AdService adService) {
        this.adService = adService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setChatRoomService(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @GetMapping("/ad/{adId}/chat")
    @JsonView(Views.MessageBasic.class)
    public ResponseEntity<List<Message>> getChatRoom(
            @PathVariable(value = "adId") Long adId
    ){
        logger.debug("getChatRoom...");
        User currentUser = getUser();
        User recipient = adService.findAdById(adId).getAdvertiser();
        final List<Message> chatMessages = messageService.findChatMessages(currentUser, recipient);
        return new ResponseEntity<>(chatMessages, HttpStatus.OK);
    }

    @PostMapping("/ad/{adId}/chat")
    @JsonView(Views.MessageBasic.class)
    public ResponseEntity<Message> sendMessage(
            @PathVariable(value = "adId") Long adId,
            @RequestBody MessageDto messageDto
    ) {
        logger.debug("sendMessage...");
        final User currentUser = getUser();
        final User recipient = adService.findAdById(adId).getAdvertiser();
        if (currentUser.getId().equals(recipient.getId())) {
            logger.error("sender and recipient are one person");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        final Optional<String> chatId = chatRoomService.getChatId(currentUser, recipient, true);
        final List<ChatRoom> allChatByChatId = chatRoomService.findAllChatByChatId(chatId.orElseThrow(() -> new RuntimeException("Failed to create chat with chatId: " + chatId)));
        allChatByChatId.forEach(s -> s.setAd(adService.findAdById(adId)));
        final Message message = messageService.createMessage(messageDto, currentUser, recipient, chatId.orElseThrow(() -> new RuntimeException("Failed to create chat")));
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/chats")
    @JsonView(Views.MessageBasic.class)
    public ResponseEntity<Set<ChatRoom>> getAllChat() {
        logger.debug("getAllChat...");
        User currentUser = getUser();
        Set<ChatRoom> myChatRoomList = chatRoomService.findAllChatCurrentUser(currentUser);
        return new ResponseEntity<>(myChatRoomList, HttpStatus.OK);
    }

    @GetMapping("chats/{chatId}")
    @JsonView(Views.MessageBasic.class)
    public ResponseEntity<List<Message>> getMessagesByChatId(
            @PathVariable(value = "chatId") String chatId
    ) {
        logger.debug("getMessagesByChatId...");
        User currentUser = getUser();
        ChatRoom chatRoom = chatRoomService.getChatByChatId(chatId);
        if (chatRoom.getSender().getId().equals(currentUser.getId()) || chatRoom.getRecipient().getId().equals(currentUser.getId())) {
            List<Message> messages = messageService.findMessagesByChatId(chatId);
            final List<Message> messageList = messages.stream()
                    .sorted(Comparator.comparing(Message::getCreationDate))
                    .collect(Collectors.toList());
            messageList.forEach(s -> {
                if (s.getRecipient().getId().equals(currentUser.getId())) {
                    s.setStatus(MessageStatus.DELIVERED);
                    messageService.updateStatuses(s.getSender(), s.getRecipient(), s.getStatus());
                }
            });
            return new ResponseEntity<>(messageList, HttpStatus.OK);
        }
        logger.error("the current user does not have rights to this chat room");
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("chats/{chatId}")
    @JsonView(Views.MessageBasic.class)
    public ResponseEntity<Message> sendMessageToChat(
            @PathVariable(value = "chatId") String chatId,
            @RequestBody MessageDto messageDto
    ) {
        logger.debug("sendMessageToChat...");
        User currentUser = getUser();
        ChatRoom chatRoom = chatRoomService.getChatByChatId(chatId);
        User recipient;
        if (chatRoom.getSender().getId().equals(currentUser.getId())) {
            recipient = chatRoom.getRecipient();
        } else if (chatRoom.getRecipient().getId().equals(currentUser.getId())){
            recipient = chatRoom.getSender();
        } else {
            logger.error("the current user does not have rights to this chat room");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        final Message message = messageService.createMessage(messageDto, currentUser, recipient, chatId);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    private User getUser() {
        UserDetailsImpl sender = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findUserByEmail(sender.getEmail());
    }
}
