package ru.satird.pcs.domains;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat_room")
public class ChatRoom implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String chatId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender", referencedColumnName = "id")
    private User sender;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipient", referencedColumnName = "id")
    private User recipient;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ad_id", referencedColumnName = "id")
    private Ad ad;
}
