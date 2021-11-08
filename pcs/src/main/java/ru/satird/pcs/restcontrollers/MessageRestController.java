package ru.satird.pcs.restcontrollers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.satird.pcs.domains.ChatRoom;
import ru.satird.pcs.domains.User;
import ru.satird.pcs.dto.chat.ChatRoomDto;
import ru.satird.pcs.dto.chat.MessageDto;
import ru.satird.pcs.dto.chat.NewMessageDto;
import ru.satird.pcs.services.AdService;
import ru.satird.pcs.services.chat.ChatRoomService;
import ru.satird.pcs.services.chat.MessageService;
import ru.satird.pcs.util.Util;

import java.util.List;
import java.util.Set;

@Api(description = "Контроллер личных сообщений")
@Slf4j
@RestController
@RequestMapping(value = "/rest/api")
public class MessageRestController {

    private final MessageService messageService;
    private final ChatRoomService chatRoomService;
    private final AdService adService;
    private final Util util;

    public MessageRestController(MessageService messageService, ChatRoomService chatRoomService, AdService adService, Util util) {
        this.messageService = messageService;
        this.chatRoomService = chatRoomService;
        this.adService = adService;
        this.util = util;
    }

    @ApiOperation(value = "Получить переписку", notes = "Получить свою переписку по данному объявлению")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adId", value = "id объявления", required = true, dataTypeClass = Long.class, paramType = "path", example = "3"),
    })
    @GetMapping("/ad/{adId}/chat")
    public ResponseEntity<List<MessageDto>> getChatRoom(
            @PathVariable(value = "adId") Long adId
    ){
        log.debug("getChatRoom...");
        User currentUser = util.getCurrentUser();
        User recipient = adService.findAdById(adId).getAdvertiser();
        final List<MessageDto> chatMessages = messageService.findChatMessages(currentUser, recipient);
        return new ResponseEntity<>(chatMessages, HttpStatus.OK);
    }

    @ApiOperation(value = "Написать сообщение", notes = "Написать сообщение автору объявления")
    @ApiImplicitParams ({
            @ApiImplicitParam(name = "adId", value = "id объявления", required = true, dataTypeClass = Long.class, paramType = "path", example = "4"),
            @ApiImplicitParam (name = "messageDto", value = "Объект с сообщением", required = true, dataTypeClass = NewMessageDto.class, paramType = "body")
    })
    @PostMapping("/ad/{adId}/chat")
    public ResponseEntity<MessageDto> sendMessage(
            @PathVariable(value = "adId") Long adId,
            @RequestBody NewMessageDto messageDto
    ) {
        log.debug("sendMessage...");
        final User currentUser = util.getCurrentUser();
        final User recipient = adService.findAdById(adId).getAdvertiser();
        if (currentUser.getId().equals(recipient.getId())) {
            log.error("sender and recipient are one person");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        final MessageDto message = messageService.addMessage(adId, messageDto, currentUser, recipient);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Получить все чаты пользователя", notes = "Получить все чаты пользователя")
    @GetMapping("/chats")
    public ResponseEntity<Set<ChatRoomDto>> getAllChat() {
        log.debug("getAllChat...");
        User currentUser = util.getCurrentUser();
        Set<ChatRoomDto> myChatRoomList = chatRoomService.findAllChatCurrentUser(currentUser);
        return new ResponseEntity<>(myChatRoomList, HttpStatus.OK);
    }

    @ApiOperation(value = "Получить чат", notes = "Получить список сообщений конкретного чата по его id")
    @ApiImplicitParams ({
            @ApiImplicitParam(name = "chatId", value = "id чата", required = true, dataTypeClass = String.class, paramType = "path", example = "1"),
    })
    @GetMapping("chats/{chatId}")
    public ResponseEntity<List<MessageDto>> getMessagesByChatId(
            @PathVariable(value = "chatId") String chatId
    ) {
        log.debug("getMessagesByChatId...");
        User currentUser = util.getCurrentUser();
        ChatRoom chatRoom = chatRoomService.getChatByChatId(chatId);
        if (chatRoom.getSender().getId().equals(currentUser.getId()) || chatRoom.getRecipient().getId().equals(currentUser.getId())) {
            final List<MessageDto> messageList = messageService.getMessagesFromChat(chatId, currentUser);
            return new ResponseEntity<>(messageList, HttpStatus.OK);
        }
        log.error("the current user does not have rights to this chat room");
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }



    @ApiOperation(value = "Написать сообщение", notes = "Написать личное сообщение в чат")
    @ApiImplicitParams ({
            @ApiImplicitParam(name = "chatId", value = "id чата", required = true, dataTypeClass = String.class, paramType = "path", example = "3"),
            @ApiImplicitParam (name = "messageDto", value = "Объект с сообщением", required = true, dataTypeClass = NewMessageDto.class, paramType = "body")
    })
    @PostMapping("chats/{chatId}")
    public ResponseEntity<MessageDto> sendMessageToChat(
            @PathVariable(value = "chatId") String chatId,
            @RequestBody NewMessageDto messageDto
    ) {
        log.debug("sendMessageToChat...");
        User currentUser = util.getCurrentUser();
        ChatRoom chatRoom = chatRoomService.getChatByChatId(chatId);
        User recipient;
        if (chatRoom.getSender().getId().equals(currentUser.getId())) {
            recipient = chatRoom.getRecipient();
        } else if (chatRoom.getRecipient().getId().equals(currentUser.getId())){
            recipient = chatRoom.getSender();
        } else {
            log.error("the current user does not have rights to this chat room");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        final MessageDto message = messageService.createMessageAndConvertToDto(messageDto, currentUser, recipient, chatId);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }
}
