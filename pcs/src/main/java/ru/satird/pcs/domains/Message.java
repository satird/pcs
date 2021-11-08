package ru.satird.pcs.domains;


import lombok.*;
import ru.satird.pcs.dto.chat.MessageStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "message")
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "message_from", referencedColumnName = "id")
    private User sender;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "message_to", referencedColumnName = "id")
    private User recipient;
    @Column(name = "text")
    private String text;
    @Column(updatable = false, name = "creation_date")
    private Date creationDate;
    @Column(name = "chat_id")
    private String chatId;
    @Column(name = "message_status")
    private MessageStatus status;
}
