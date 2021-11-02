package ru.satird.pcs.domains;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.satird.pcs.util.Views;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "comment")
@EqualsAndHashCode(of = {"id"})
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonView(Views.CommentIdText.class)
    private Long id;
    @Column(name = "text")
    @JsonView(Views.CommentIdText.class)
    private String text;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    @JsonView(Views.CommentIdText.class)
    private User author;
    @ManyToOne
    @JoinColumn(name = "ad_id")
    @JsonView(Views.FullComment.class)
    private Ad ad;
    @Column(updatable = false, name = "creation_date")
    @JsonView(Views.CommentIdTextDate.class)
    private Date creationDate;
}
