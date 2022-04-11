package ch.unige.pinfo3.domain.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;

@Entity
@Table(name = "USERS")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public UUID uuid;

    @NotNull
    public String username;
    
    @NotNull
    public String email;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private List<Search> searches;

    public User() {}    // required to be able to marshall objects.

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.uuid = UUID.randomUUID();
    }
}
