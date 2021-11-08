package ru.satird.pcs.mapper;

import org.mapstruct.Mapper;
import ru.satird.pcs.domains.ChatRoom;
import ru.satird.pcs.dto.chat.ChatRoomDto;

import java.util.Set;

@Mapper
public interface ChatRoomMapper {

    ChatRoom mapChatRoom(ChatRoomDto chatRoomDto);
    ChatRoomDto mapChatRoomDto(ChatRoom chatRoom);
    Set<ChatRoom> mapChatRoomList(Set<ChatRoomDto> chatRoomDtoList);
    Set<ChatRoomDto> mapChatRoomDtoList(Set<ChatRoom> chatRoomList);
}
