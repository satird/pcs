package ru.satird.pcs.domains;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.satird.pcs.util.Views;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name"),
        @UniqueConstraint(columnNames = "email")
})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonView({Views.AdBasic.class, Views.CommentIdText.class, Views.MessageBasic.class})
    private Long id;
    @NotBlank
    @Column(name = "name")
    @JsonView({Views.AdBasic.class, Views.CommentIdText.class, Views.MessageBasic.class})
    private String name;
    @NotBlank
    @Column(name = "password")
    private String password;
    @NotBlank
    @Email
    @Column(name = "email")
    @JsonView({Views.AdBasic.class, Views.CommentIdText.class})
    private String email;
    @Column(name = "active")
    private boolean active;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    @Column(name = "rating")
    private Float rating;

    public User(@NotBlank String name, @NotBlank @Email String email, @NotBlank String password ) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public User(Long id) {
        this.id = id;
    }
}
