package ru.satird.pcs.mapper;

import org.mapstruct.Mapper;
import ru.satird.pcs.domains.Message;
import ru.satird.pcs.dto.chat.MessageDto;

import java.util.List;

@Mapper
public interface MessageMapper {

    Message mapMessage(MessageDto messageDto);
    MessageDto mapMessageDto(Message message);
    List<Message> mapMessageList(List<MessageDto> messageDtoList);
    List<MessageDto> mapMessageDtoList(List<Message> messageList);
}
