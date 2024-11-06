package usfca.pyne.cs601.virtualfridge.Model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import usfca.pyne.cs601.virtualfridge.Entity.UserEntity;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "fridges")
public class Fridge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "fridge", cascade = CascadeType.ALL)
    private List<Ingredient> ingredients;

    public Fridge() {}

    public Fridge(Long id, UserEntity user, List<Ingredient> ingredients) {
        this.id = id;
        this.user = user;
        this.ingredients = ingredients;
    }
}
