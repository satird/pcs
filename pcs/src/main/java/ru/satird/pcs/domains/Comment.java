package ru.satird.pcs.domains;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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
    private Long id;
    @Column(name = "text")
    private String text;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User author;
    @ManyToOne
    @JoinColumn(name = "ad_id")
    private Ad ad;
    @Column(updatable = false, name = "creation_date")
    private Date creationDate;
}
