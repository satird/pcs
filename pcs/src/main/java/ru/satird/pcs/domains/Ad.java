package ru.satird.pcs.domains;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "text")
    private String text;
    @Column(name = "price")
    private Float price;
    @Column(name = "premium")
    private boolean premium;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User advertiser;
    @Column(name = "category")
    private Category category;
    @Column(updatable = false, name = "creation_date")
    private Date creationDate;
    @OneToMany(mappedBy = "ad", orphanRemoval = true)
    private List<Comment> comments;
}
