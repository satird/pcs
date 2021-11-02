package ru.satird.pcs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.satird.pcs.domains.ChatRoom;
import ru.satird.pcs.domains.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {

    Optional<ChatRoom> findBySenderAndRecipient(User senderId, User recipientId);
    Optional<Set<ChatRoom>> findAllBySender(User user);
    Optional<ChatRoom> findFirstByChatId(String chatId);
    Optional<List<ChatRoom>> findAllByChatId(String chatId);
}
