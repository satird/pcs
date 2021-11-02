package ru.satird.pcs.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.satird.pcs.domains.Message;
import ru.satird.pcs.dto.chat.MessageStatus;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByChatId(String chatId);

    @Modifying
    @Transactional
    @Query("update Message m set m.status = :status where m.sender.id = :senderId and m.recipient.id = :recipientId")
    void updateStatus(Long senderId, Long recipientId, MessageStatus status);
}