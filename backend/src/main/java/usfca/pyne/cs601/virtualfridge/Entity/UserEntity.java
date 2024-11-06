package usfca.pyne.cs601.virtualfridge.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import usfca.pyne.cs601.virtualfridge.Model.FavoriteRecipe;
import usfca.pyne.cs601.virtualfridge.Model.Fridge;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String firstName;
    private String lastName;

    @Column(nullable = false)
    private String email;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Fridge fridge;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<FavoriteRecipe> favoriteRecipes;


    public UserEntity(long id, String username, String firstName, String lastName, String email) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public UserEntity() {
    }

}
