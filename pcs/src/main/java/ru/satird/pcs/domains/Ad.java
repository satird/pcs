package ru.satird.pcs.domains;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.satird.pcs.util.Views;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ad")
public class Ad implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonView({Views.AdBasic.class, Views.MessageBasic.class})
    private Long id;
    @JsonView({Views.AdBasic.class, Views.MessageBasic.class})
    @Column(name = "title")
    private String title;
    @JsonView(Views.AdBasic.class)
    @Column(name = "text")
    private String text;
    @JsonView(Views.AdBasic.class)
    @Column(name = "price")
    private Float price;
    @JsonView(Views.AdBasic.class)
    @Column(name = "premium")
    private boolean premium;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonView(Views.AdBasic.class)
    private User advertiser;
    @Column(name = "category")
    @JsonView(Views.AdBasic.class)
    private Category category;
    @Column(updatable = false, name = "creation_date")
    @JsonView(Views.AdBasic.class)
    private Date creationDate;
    @JsonView(Views.AdBasicAndComments.class)
    @OneToMany(mappedBy = "ad", orphanRemoval = true)
    private List<Comment> comments;
}
