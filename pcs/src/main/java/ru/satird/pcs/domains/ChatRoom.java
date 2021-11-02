package ru.satird.pcs.domains;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import ru.satird.pcs.util.Views;

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
    @JsonView(Views.MessageBasic.class)
    private Long id;
    @JsonView(Views.MessageBasic.class)
    private String chatId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender", referencedColumnName = "id")
    @JsonView(Views.MessageBasic.class)
    private User sender;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipient", referencedColumnName = "id")
    @JsonView(Views.MessageBasic.class)
    private User recipient;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ad_id", referencedColumnName = "id")
    @JsonView(Views.MessageBasic.class)
    private Ad ad;
}
